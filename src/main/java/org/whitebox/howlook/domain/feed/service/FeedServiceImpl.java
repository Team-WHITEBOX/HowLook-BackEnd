package org.whitebox.howlook.domain.feed.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.dto.HashtagDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.Hashtag;
import org.whitebox.howlook.domain.feed.entity.Scrap;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;
import org.whitebox.howlook.domain.feed.repository.HashtagRepository;
import org.whitebox.howlook.domain.feed.repository.ScrapRepository;
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
                String m_path = uploadPath+"\\"+uuid+"_"+originalName;


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

        Feed feed = result.orElseThrow();
        log.info(feed);

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        FeedReaderDTO feedReaderDTO = modelMapper.map(feed, FeedReaderDTO.class);

        feedReaderDTO.setHashtagDTO(new HashtagDTO(feed.getHashtag()));
        feedReaderDTO.setUserPostInfo(new UserPostInfoResponse(feed.getMember()));

        // 사진 경로 가져오기
        feedReaderDTO.setPhotoPaths(uploadService.getPath(NPostId));

        return feedReaderDTO;
    }

    @Override
    public List<FeedReaderDTO> readerUID(String UserID) {
        List<Feed> feeds = feedRepository.findByMid(UserID);
        List<FeedReaderDTO> result = new ArrayList<>();
        for(Feed feed : feeds){
            FeedReaderDTO feedReaderDTO = new FeedReaderDTO().builder()
                    .NPostId(feed.getNPostId()).userPostInfo(new UserPostInfoResponse(feed.getMember()))
                    .PhotoCnt(feed.getPhotoCnt()).LikeCount(feed.getLikeCount()).CommentCount(feed.getCommentCount())
                    .ViewCnt(feed.getViewCnt()).Content(feed.getContent()).MainPhotoPath(feed.getMainPhotoPath())
                    .modDate(feed.getModDate()).regDate(feed.getRegDate()).build();
            result.add(feedReaderDTO);
        }
        return result;
    }

    @Override
    public Page<FeedReaderDTO> getFeedPage(int size, int page) {
        final Pageable pageable = PageRequest.of(page, size);
        log.info(pageable);
        log.info(pageable.getOffset());
        Page<FeedReaderDTO> feedPage = feedRepository.findFeedReaderDTOPage(pageable);
        feedPage.forEach(feedReaderDTO -> {
            feedReaderDTO.setPhotoPaths(uploadService.getPath(feedReaderDTO.getNPostId()));
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

}
