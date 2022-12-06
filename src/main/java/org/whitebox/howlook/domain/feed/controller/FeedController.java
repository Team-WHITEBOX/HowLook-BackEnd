package org.whitebox.howlook.domain.feed.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.dto.HashtagDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.service.FeedService;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.ErrorResponse;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.*;


@RestController
@RequestMapping("/feed")
@Log4j2
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    //게시글 등록하는 POST로 매핑된 API구현
    @PostMapping(value = "/register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> registerPost(@Valid @ModelAttribute FeedRegisterDTO feedRegisterDTO) {
        log.info("Feed POST register!");

        log.info(feedRegisterDTO);

        feedService.registerPOST(feedRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(CREATE_POST_SUCCESS));
    }

    //게시글 삭제하는 API
    @DeleteMapping(value = "/delete")
    public ResponseEntity<ResultResponse> deletePost(@RequestParam Long npost_id) {
        feedService.deleteFeed(npost_id);

        return ResponseEntity.ok(ResultResponse.of(DELETE_POST_SUCCESS));
    }

    //게시물 불러오는 GET으로 매핑한 API구현해야함
    @GetMapping("/readbypid")
    public ResponseEntity<ResultResponse> readFeedbyPID(Long NPostId) {
        FeedReaderDTO feedReaderDTO = feedService.readerPID(NPostId);

        log.info(feedReaderDTO);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS, feedReaderDTO));
    }

    @GetMapping("/readbyuid")
    public ResponseEntity<ResultResponse> readFeedbyUID(String UserID) {
        List<FeedReaderDTO> feeds = feedService.readerUID(UserID);

        log.info(feeds);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS,feeds));
    }

    @GetMapping("/recent")
    public ResponseEntity<ResultResponse> getRecent10Posts(@RequestParam int page) {
        final List<FeedReaderDTO> postList = feedService.getFeedPage(2, page).getContent();

        return ResponseEntity.ok(ResultResponse.of(FIND_RECENT10POSTS_SUCCESS, postList));
    }

    @PostMapping("/scrap")
    public ResponseEntity<ResultResponse> scrapFeed(@RequestParam Long npost_id){
        feedService.scrapFeed(npost_id);
        return ResponseEntity.ok(ResultResponse.of(BOOKMARK_POST_SUCCESS));
    }

    @DeleteMapping("/scrap")
    public ResponseEntity<ResultResponse> unScrapFeed(@RequestParam Long npost_id) {
        feedService.unScrapFeed(npost_id);

        return ResponseEntity.ok(ResultResponse.of(UN_BOOKMARK_POST_SUCCESS));
    }

    //feeds에 값이 없으면 Error 반환해야함. 차후 설정필요
    @GetMapping("/search")
    @ApiOperation(value = "카테고리로 피드검색", notes= "카테고리(해시태그 + 사용자정보(키, 몸무게, 성별)로 피드검색함\n해시태그는 반드시 true/false중 하나 선택해야함.(비워두면 안됨)\n" +
            "page에는 표시하고 싶은 page번호 입력(0부터시작), 현재 한 페이지당 post 5개씩 출력됨.")
    public ResponseEntity<ResultResponse> searchByCategories(HashtagDTO hashtagDTO, Long heightHigh, Long heightLow, Long weightHigh, Long weightLow, char gender, int page) {
        List<FeedReaderDTO> feeds = feedService.searchFeedByHashtag(hashtagDTO, heightHigh, heightLow, weightHigh, weightLow, gender, page, 5);

        return ResponseEntity.ok(ResultResponse.of(GET_HASHTAG_FEED_SUCCESS, feeds));
    }
}
