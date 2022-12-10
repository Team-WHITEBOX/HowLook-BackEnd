package org.whitebox.howlook.domain.feed.service;

import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.dto.HashtagDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.FeedLike;
import org.whitebox.howlook.domain.feed.entity.Hashtag;
import org.whitebox.howlook.domain.feed.entity.Scrap;
import org.whitebox.howlook.domain.feed.repository.*;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;
import org.whitebox.howlook.domain.upload.dto.UploadResultDTO;
import org.whitebox.howlook.domain.upload.entity.Upload;
import org.whitebox.howlook.domain.upload.repository.UploadRepository;
import org.whitebox.howlook.domain.upload.service.UploadService;
import org.whitebox.howlook.global.error.exception.EntityAlreadyExistException;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.whitebox.howlook.global.error.ErrorCode.*;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
@Data
public class FeedServiceImpl implements  FeedService{

    private final ModelMapper modelMapper;
    private final FeedRepository feedRepository;
    private final HashtagRepository hashtagRepository;
    private final UploadRepository uploadRepository;
    private final ScrapRepository scrapRepository;
    private final AccountUtil accountUtil;
    private final FeedLikeRepository feedLikeRepository;
    private final UploadService uploadService; // 업로드 서비스
    @Value("${org.whitebox.upload.path}")
    private String uploadPath; // 저장될 경로

    //전달받은 FeedRegisterDTO값을 데이터베이스에 저장
    //해당 Post 자체데이터 + 사진데이터 테이블 + 해시테그 테이블 함께저장
    @Override
    public void registerPOST(FeedRegisterDTO feedRegisterDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Feed feed = modelMapper.map(feedRegisterDTO, Feed.class);
        feed.setMember(accountUtil.getLoginMember());
        feed.setLikeCount(0L);
        feed.setCommentCount(0L);
        HashtagDTO hashtagDTO = feedRegisterDTO.getHashtagDTO();
        Hashtag hashtag = modelMapper.map(hashtagDTO, Hashtag.class);
        hashtag.setFeed(feed);

        hashtagRepository.save(hashtag);

        feed.setHashtag(hashtag);
        feedRepository.save(feed);
        UploadFileDTO uploadFileDTO = feedRegisterDTO.getUploadFileDTO();
        // 사진 업로드 코드
        log.info(uploadFileDTO);

        if(uploadFileDTO.getFiles() != null)
        {
            for(int i = 0; i < uploadFileDTO.getFiles().size(); i++)
            {
                var multipartFile = uploadFileDTO.getFiles().get(i);
                String originalName = multipartFile.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();

                Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);

                try{
                    multipartFile.transferTo(savePath);
                }catch (IOException e)
                {
                    e.printStackTrace();
                }

                // 여기까지는 사진을 Server에 저장하는 코드
                // 여기서부터 사진 정보를 DB에 저장하는 코드
                // UploadFileDTO를 통해 db에 사진 저장경로를 Insert
                //String m_path = uploadPath+"\\"+uuid+"_"+originalName;
                String m_path = uuid+"_"+originalName;


                // Falcon : MainPhotoPath 및 PhotoCnt 저장하기
                if(i == 0)
                {
                    feed.setMainPhotoPath(m_path);
                }

                if(i+1 == uploadFileDTO.getFiles().size())
                {
                    feed.setPhotoCnt(Long.valueOf(i+1));
                }

                UploadResultDTO temp = UploadResultDTO.builder().Path(m_path).feed(feed).build();
                uploadService.register(temp);

            }
        }
    }

    @Override
    public FeedReaderDTO readerPID(Long NPostId) {
        Optional<Feed> result = feedRepository.findById(NPostId);
        Member member = accountUtil.getLoginMember();
        Feed feed = result.orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        log.info(feed);

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        FeedReaderDTO feedReaderDTO = modelMapper.map(feed, FeedReaderDTO.class);

        feedReaderDTO.setHashtagDTO(new HashtagDTO(feed.getHashtag()));
        feedReaderDTO.setUserPostInfo(new UserPostInfoResponse(feed.getMember()));

        // 사진 경로 가져오기
        feedReaderDTO.setPhotoDTOs(uploadService.getPhtoData(NPostId));

        // 유저의 게시글 좋아요 체크
        if(feedLikeRepository.findByMemberAndFeed(member,feed).isPresent()) {
            feedReaderDTO.setLike_chk(true);
        }

        else {
            feedReaderDTO.setLike_chk(false);
        }

        return feedReaderDTO;
    }

    @Override
    public List<FeedReaderDTO> readerUID(String UserID) {
        List<Feed> feeds = feedRepository.findByMid(UserID);
        List<FeedReaderDTO> result = feeds.stream().map(feed ->  new FeedReaderDTO(feed)).collect(Collectors.toList());
        result.forEach( feedReaderDTO -> feedReaderDTO.setPhotoDTOs(uploadService.getPhtoData(feedReaderDTO.getNPostId())));
        return result;
    }

    @Override
    public Page<FeedReaderDTO> getFeedPage(int size, int page) {
        final Pageable pageable = PageRequest.of(page, size);
        log.info(pageable);
        log.info(pageable.getOffset());
        Page<FeedReaderDTO> feedPage = feedRepository.findFeedReaderDTOPage(pageable);
        feedPage.forEach(feedReaderDTO -> {
            feedReaderDTO.setPhotoDTOs(uploadService.getPhtoData(feedReaderDTO.getNPostId()));
        });
        return feedPage;
    }

    @Override
    public Page<FeedReaderDTO> getNearFeedPage(int size,int page,float latitude, float longitude)
    {
        final Pageable pageable = PageRequest.of(page, size);

        Page<FeedReaderDTO> feedPage = feedRepository.findNearFeedReaderDTOPage(pageable, latitude, longitude);
        feedPage.forEach(feedReaderDTO -> {
            feedReaderDTO.setPhotoDTOs(uploadService.getPhtoData(feedReaderDTO.getNPostId()));
        });
        return feedPage;
    }

    @Override
    public void scrapFeed(Long npost_id) {
        final Feed feed = feedRepository.findById(npost_id).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        final Member member = accountUtil.getLoginMember();

        if(scrapRepository.findByMemberAndFeed(member,feed).isPresent()){
            throw new EntityAlreadyExistException(SCRAP_ALREADY_EXIST);
        }
        Scrap scrap = new Scrap(member,feed);
        scrapRepository.save(scrap);
    }

    @Override
    public void unScrapFeed(Long npost_id) {
        final Feed feed = feedRepository.findById(npost_id).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        final Member member = accountUtil.getLoginMember();

        Scrap scrap = scrapRepository.findByMemberAndFeed(member,feed).orElseThrow(()-> new EntityNotFoundException(SCRAP_NOT_FOUND));
        scrapRepository.delete(scrap);
    }

    @Override
    public List<FeedReaderDTO> searchFeedByHashtag(HashtagDTO hashtagDTO, Long heightHigh, Long heightLow, Long weightHigh, Long weightLow, char gender,
                                                   int page, int size) {
        final Pageable pageable = PageRequest.of(page, size);
        //해당 함수를 통해 hashtagDTO에서 true로 설정된 값만 있는 feed의 ID를 불러온다.
        List<FeedReaderDTO> feeds = feedRepository.findFeedByCategories(hashtagDTO, heightHigh, heightLow, weightHigh, weightLow, gender, pageable);


        return feeds;
    }

    @Override
    @ApiOperation(value = "게시글 해시태그와 함께 삭제된다. 사진은 삭제되지 않음")
    public void deleteFeed(Long npost_id) {
        final Feed feed = feedRepository.findById(npost_id).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        Long hashtagid = feed.getHashtag().getHashtagId();

        final Hashtag hashtag = hashtagRepository.findById(hashtagid).orElseThrow(() -> new EntityNotFoundException(HASHTAG_NOT_FOUND));
        log.info(hashtagid);

        final List<Upload> uploads = uploadRepository.findByPostId(npost_id);
        for(Upload u : uploads)
        {
            u.setFeed(null);
        }

        hashtagRepository.delete(hashtag);
        feedRepository.delete(feed);
    }
    @Override
    public void likeFeed(Long NPostId) {
        Feed feed = feedRepository.findById(NPostId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        Member member = accountUtil.getLoginMember();

        if(feedLikeRepository.findByMemberAndFeed(member,feed).isPresent()) {
            throw new EntityAlreadyExistException(POST_LIKE_ALREADY_EXIST);
        }

        feed.UplikeCount();
        feedLikeRepository.save(new FeedLike(member,feed));
    }

    @Override
    public void unlikeFeed(Long NPostId) {
        Feed feed = feedRepository.findById(NPostId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        Member member = accountUtil.getLoginMember();
        feed.DownLikeCount();
        FeedLike feedLike = feedLikeRepository.findByMemberAndFeed(member,feed).orElseThrow();
        feedLikeRepository.delete(feedLike);
    }
}
