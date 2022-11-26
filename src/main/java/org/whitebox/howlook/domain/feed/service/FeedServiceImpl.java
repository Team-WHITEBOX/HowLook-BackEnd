package org.whitebox.howlook.domain.feed.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.dto.HashtagDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.Hashtag;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;
import org.whitebox.howlook.domain.upload.dto.UploadResultDTO;
import org.whitebox.howlook.domain.upload.repository.UploadRepository;
import org.whitebox.howlook.domain.upload.service.UploadService;
import org.whitebox.howlook.global.util.AccountUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
@Data
public class FeedServiceImpl implements  FeedService{

    private final ModelMapper modelMapper;
    private final FeedRepository feedRepository;

    private final UploadRepository uploadRepository;
    private final AccountUtil accountUtil;
    private final UploadService uploadService; // 업로드 서비스
    private final HashtagService hashtagService;
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
        hashtagService.registerHashtag(hashtagDTO);

        feedRepository.save(feed);

        UploadFileDTO uploadFileDTO = feedRegisterDTO.getUploadFileDTO();
        // 사진 업로드 코드
        log.info(uploadFileDTO);
        final List<UploadResultDTO> list = new ArrayList<>();

        if(uploadFileDTO.getFiles() != null)
        {
            // forEach 문으로 선택한 사진 수 만큼 실행 됨
            uploadFileDTO.getFiles().forEach(multipartFile -> {
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
                Long pId = uploadFileDTO.getNPostId();
                log.info(pId);
                //UploadResultDTO temp = UploadResultDTO.builder().Path(uploadPath+"\\"+uuid+"_"+originalName).NPostId(pId).build();
                UploadResultDTO temp = UploadResultDTO.builder().Path(uploadPath+"\\"+uuid+"_"+originalName).feed(feed).build();
                list.add(temp);
                log.info(pId);
                log.info(temp);
                uploadService.register(temp);

            });
        }
//
//        Upload upload = new Upload(feed, "C:\\upload");
//        uploadRepository.save(upload);
        //feed.getPhotoCnt()
    }

    @Override
    public FeedReaderDTO readerPID(Long NPostId) {
        Optional<Feed> result = feedRepository.findById(NPostId);

        Feed feed = result.orElseThrow();
        log.info(feed);

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        FeedReaderDTO feedReaderDTO = modelMapper.map(feed, FeedReaderDTO.class);
        feedReaderDTO.setUserPostInfo(new UserPostInfoResponse(feed.getMember()));

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
        //log.info(feed);

        //modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        //FeedReaderDTO feedReaderDTO = modelMapper.map(feed, FeedReaderDTO.class);

        return result;
    }
}