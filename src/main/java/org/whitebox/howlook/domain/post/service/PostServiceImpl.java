package org.whitebox.howlook.domain.post.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.domain.post.dto.*;
import org.whitebox.howlook.domain.post.entity.*;
import org.whitebox.howlook.domain.post.repository.*;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;
import org.whitebox.howlook.domain.upload.dto.UploadResultDTO;
import org.whitebox.howlook.domain.upload.entity.Upload;
import org.whitebox.howlook.domain.upload.repository.UploadRepository;
import org.whitebox.howlook.domain.upload.service.UploadService;
import org.whitebox.howlook.global.error.exception.EntityAlreadyExistException;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;
import org.whitebox.howlook.global.util.LocalUploader;
import org.whitebox.howlook.global.util.S3Uploader;
import org.whitebox.howlook.global.util.WeatherUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.whitebox.howlook.global.error.ErrorCode.*;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
@Data
public class PostServiceImpl implements PostService {
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;

    private final ReplyLikeRepository replyLikeRepository;

    private final ReplyServiceImpl replyService;

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final UploadRepository uploadRepository;
    private final ScrapRepository scrapRepository;
    private final AccountUtil accountUtil;
    private final PostLikeRepository postLikeRepository;
    private final UploadService uploadService; // 업로드 서비스
    @Value("${org.whitebox.upload.path}")
    private String uploadPath; // 저장될 경로
    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;
    private final WeatherUtil weatherUtil;
    @Value("${org.whitebox.server.upload}")
    public String isServer;

    //전달받은 postRegisterDTO값을 데이터베이스에 저장
    //해당 Post 자체데이터 + 사진데이터 테이블 + 해시테그 테이블 함께저장
    @Transactional
    @Override
    public List<String> registerPOST(PostRegisterDTO postRegisterDTO) throws IOException, ParseException {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Post post = modelMapper.map(postRegisterDTO, Post.class);
        post.setMember(accountUtil.getLoginMember());
        post.setLikeCount(0L);
        post.setPostReplyCount(0L);
        HashtagDTO hashtagDTO = postRegisterDTO.getHashtagDTO();
        Hashtag hashtag = modelMapper.map(hashtagDTO, Hashtag.class);
        hashtag.setpost(post);

        hashtagRepository.save(hashtag);

        // 날씨를 post에 저장해야함
        WeatherDTO weatherDTO = weatherUtil.getWeather(postRegisterDTO.getLatitude(), postRegisterDTO.getLongitude());

        post.setTemperature(weatherDTO.getTemperature());
        post.setWeather(weatherDTO.getWeather());

        post.setHashtag(hashtag);

        postRepository.save(post);
        log.info(post.getMainPhotoPath());

        UploadFileDTO uploadFileDTO = postRegisterDTO.getUploadFileDTO();

        List<String> uploadedFilePaths = new ArrayList<>();
        for(MultipartFile file:uploadFileDTO.getFiles()){
            uploadedFilePaths.addAll(localUploader.uploadLocal(file));
        }

        List<String> s3Paths = new ArrayList<>();

        if(isServer.equals("true")) {
            s3Paths = uploadedFilePaths.stream().map(s3Uploader::upload).collect(Collectors.toList());
        }

        // 사진 업로드 코드
        if(uploadFileDTO.getFiles() != null)
        {
            for(int i = 0; i < uploadFileDTO.getFiles().size(); i++)
            {
                String m_path;

                if(isServer.equals("true")) {
                    m_path = s3Paths.get(i);
                }
                else
                {
                    m_path = uploadedFilePaths.get(i);
                }

                // Falcon : MainPhotoPath 및 PhotoCnt 저장하기
                if(i == 0)
                {
                    post.setMainPhotoPath(m_path);
                }

                if(i+1 == uploadFileDTO.getFiles().size())
                {
                    post.setPhotoCount(Long.valueOf(i+1));
                }

                UploadResultDTO temp = UploadResultDTO.builder().path(m_path).post(post).build();
                uploadService.register(temp);
                log.info(post.getMainPhotoPath());
            }
        }

        return s3Paths;
    }

    @Override
    public PostReaderDTO readerPID(Long postId) {
        Optional<Post> result = postRepository.findById(postId);
        Member member = accountUtil.getLoginMember();
        Post post = result.orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        log.info(post);
        PostReaderDTO postReaderDTO = new PostReaderDTO(post);

        // 사진 경로 가져오기
        postReaderDTO.setPhotoDTOs(uploadService.getPhotoData(postId));

        // 유저의 게시글 좋아요 체크
        if(postLikeRepository.findByMemberAndPost(member,post).isPresent()) {
            postReaderDTO.setLikeCheck(true);
        }
        else {
            postReaderDTO.setLikeCheck(false);
        }

        if(scrapRepository.findByMemberAndPost(member,post).isEmpty()) {
            postReaderDTO.setIsScrapped(false);
        }else {
            postReaderDTO.setIsScrapped(true);
        }

        return postReaderDTO;
    }

    @Override
    public List<PostReaderDTO> readerUID(String UserID) {
        final List<Post> posts = postRepository.findByMemberId(UserID).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        final Member member = accountUtil.getLoginMember();
        //UserID에 일치하는 값 없을 경우 POST_NOT_FOUND 반환
        List<PostReaderDTO> result = posts.stream().map(post ->  new PostReaderDTO(post)).collect(Collectors.toList());
        result.forEach(postReaderDTO -> {
            postReaderDTO.setPhotoDTOs(uploadService.getPhotoData(postReaderDTO.getPostId()));

            if(postLikeRepository.findByMemberAndPostId(member,postReaderDTO.getPostId()) != null){
                postReaderDTO.setLikeCheck(true);
            }
        });

        return result;
    }

    @Override
    public Page<PostReaderDTO> getPostPage(int size, int page) {
        final Pageable pageable = PageRequest.of(page, size);
        final Member member = accountUtil.getLoginMember();

        log.info(pageable);
        log.info(pageable.getOffset());
        Page<PostReaderDTO> postPage = postRepository.findPostReaderDTOPage(pageable);

        postPage.forEach(postReaderDTO -> {
            postReaderDTO.setPhotoDTOs(uploadService.getPhotoData(postReaderDTO.getPostId()));

            // 좋아요 체크하기
            if(postLikeRepository.findByMemberAndPostId(member,postReaderDTO.getPostId()) != null){
                postReaderDTO.setLikeCheck(true);
            }
        });

        return postPage;
    }

    @Override
    public Page<PostReaderDTO> getNearPostPage(int size, int page, float latitude, float longitude)
    {
        final Pageable pageable = PageRequest.of(page, size);
        final Member member = accountUtil.getLoginMember();

        Page<PostReaderDTO> postPage = postRepository.findNearPostReaderDTOPage(pageable, latitude, longitude);
        postPage.forEach(postReaderDTO -> {
            postReaderDTO.setPhotoDTOs(uploadService.getPhotoData(postReaderDTO.getPostId()));

            // 좋아요 체크하기
            if(postLikeRepository.findByMemberAndPostId(member,postReaderDTO.getPostId()) != null){
                postReaderDTO.setLikeCheck(true);
            }
        });
        return postPage;
    }

    @Override
    public Page<PostReaderDTO> getWeatherPostPage(int size, int page, float latitude, float longitude) throws IOException, ParseException {
        final Pageable pageable = PageRequest.of(page, size);
        final Member member = accountUtil.getLoginMember();

        // 날씨를 post에 저장해야함
        WeatherDTO weatherDTO = weatherUtil.getWeather(latitude, longitude);

        Page<PostReaderDTO> postPage = postRepository.findTemperaturePostReaderDTOPage(pageable, weatherDTO.getTemperature());
        postPage.forEach(postReaderDTO -> {
            postReaderDTO.setPhotoDTOs(uploadService.getPhotoData(postReaderDTO.getPostId()));
            // 좋아요 체크하기
            if(postLikeRepository.findByMemberAndPostId(member,postReaderDTO.getPostId()) != null){
                postReaderDTO.setLikeCheck(true);
            }
        });

        return postPage;
    }


    @Transactional
    @Override
    public void scrapPost(Long postId) {
        final Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        final Member member = accountUtil.getLoginMember();

        if(scrapRepository.findByMemberAndPost(member,post).isPresent()){
            throw new EntityAlreadyExistException(SCRAP_ALREADY_EXIST);
        }

        Scrap scrap = new Scrap(member,post);
        scrapRepository.save(scrap);
    }

    @Transactional
    @Override
    public void unScrapPost(Long postId) {
        final Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        final Member member = accountUtil.getLoginMember();

        Scrap scrap = scrapRepository.findByMemberAndPost(member,post).orElseThrow(()-> new EntityNotFoundException(SCRAP_NOT_FOUND));
        scrapRepository.delete(scrap);
    }

    @Override
    public Boolean isScrapPost(Long postId) {
        final Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        final Member member = accountUtil.getLoginMember();
        Boolean check=false;
        if(scrapRepository.findByMemberAndPost(member,post).isPresent()) check=true;
        return check;
    }

    @Override
    public Page<PostReaderDTO> searchPostByHashtag(SearchCategoryDTO searchCategoryDTO) {
        final Pageable pageable = PageRequest.of(searchCategoryDTO.getPage(), searchCategoryDTO.getSize());

        //해당 함수를 통해 hashtagDTO에서 true로 설정된 값만 있는 post의 ID를 불러온다.
        Page<PostReaderDTO> postPage = postRepository.findPostByCategories(searchCategoryDTO, pageable);
        postPage.forEach(postReaderDTO -> {
            postReaderDTO.setPhotoDTOs(uploadService.getPhotoData(postReaderDTO.getPostId()));
        });

        return postPage;
    }

    @Transactional
    @Override
    public void deletePost(Long postId) {
        //우선, 해당 게시글이 있는지를 확인한다.
        final Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        Long hashtagid = post.getHashtag().getHashtagId();

        //지우려는 유저(로그인유저)와 게시글의 유저가 같은지 확인
        String thismember = accountUtil.getLoginMemberId();
        String postmember = postRepository.findMemberIdByPostId(postId);
        log.info("thismember: " + thismember + "postmember: " + postmember);
        if(!thismember.equals(postmember) && !thismember.equals("admin")) {
            throw new EntityAlreadyExistException(POST_CANT_DELETE);
        }

        final Hashtag hashtag = hashtagRepository.findById(hashtagid).orElseThrow(() -> new EntityNotFoundException(HASHTAG_NOT_FOUND));
        log.info(hashtagid);

        final List<Upload> uploads = uploadRepository.findByPostId(postId);
        for(Upload u : uploads) //연결된 사진 연결 끊기
            u.setPost(null);

        // 댓글 및 댓글 좋아요 삭제
        List<Reply> replies = replyRepository.listOfpost(postId);
        for (Reply reply : replies) {
            List<ReplyLike> replyLikes = replyLikeRepository.findByReplyId(reply.getReplyId());
            for(ReplyLike replyLike : replyLikes) {
                replyLikeRepository.delete(replyLike);
            }
            replyRepository.delete(reply);
        }

        // 스크랩 삭제
        List<Scrap> scraps = scrapRepository.findAllByPostId(postId);
        if(scraps.size() != 0) { // 게시글 삭제할때 게시글 스크랩 다 제거
            for (Scrap scrap : scraps) {
                scrapRepository.delete(scrap);
            }
        }

        //좋아요 삭제
        List<PostLike> postlikes = postLikeRepository.findAllWithMemberByPostId(postId);
        if(postlikes.size() != 0) {
            for(PostLike postlike : postlikes) {
                postLikeRepository.delete(postlike);
            }
        }
        
        hashtagRepository.delete(hashtag);
        postRepository.delete(post);
    }

    @Transactional
    @Override
    public void likePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        Member member = accountUtil.getLoginMember();

        if(postLikeRepository.findByMemberAndPost(member,post).isPresent()) {
            throw new EntityAlreadyExistException(POST_LIKE_ALREADY_EXIST);
        }

        post.UplikeCount();
        postLikeRepository.save(new PostLike(member,post));
    }

    @Transactional
    @Override
    public void unlikePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        Member member = accountUtil.getLoginMember();

        if(post.getLikeCount() == 0) {
            postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_LIKED));
        }

        post.DownLikeCount();
        PostLike postLike = postLikeRepository.findByMemberAndPost(member,post).orElseThrow();
        postLikeRepository.delete(postLike);
    }
}
