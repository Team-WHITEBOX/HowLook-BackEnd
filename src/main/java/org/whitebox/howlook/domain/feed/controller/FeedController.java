package org.whitebox.howlook.domain.feed.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.service.FeedService;
import org.whitebox.howlook.domain.member.service.MemberService;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.ErrorResponse;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.*;

import static org.whitebox.howlook.global.result.ResultCode.CREATE_POST_FAIL;
import static org.whitebox.howlook.global.result.ResultCode.REGISTER_SUCCESS;

@RestController
@RequestMapping("/feed")
@Log4j2
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @ApiOperation(value = "피드 게시글 등록")
    @PostMapping(value = "/register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> registerPost(@Valid @ModelAttribute FeedRegisterDTO feedRegisterDTO) {
        log.info("Feed POST register!");

        log.info(feedRegisterDTO);

        feedService.registerPOST(feedRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(CREATE_POST_SUCCESS));
    }

    @ApiOperation(value = "게시글 id로 피드 게시글 조회")
    @GetMapping("/readbypid")
    public ResponseEntity<ResultResponse> readFeedbyPID(Long NPostId) {
        FeedReaderDTO feedReaderDTO = feedService.readerPID(NPostId);

        log.info(feedReaderDTO);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS, feedReaderDTO));
    }
    @ApiOperation(value = "멤버 id로 피드 게시글 모두 조회")
    @GetMapping("/readbyuid")
    public ResponseEntity<ResultResponse> readFeedbyUID(String UserID) {
        List<FeedReaderDTO> feeds = feedService.readerUID(UserID);

        log.info(feeds);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS,feeds));
    }
    @ApiOperation(value = "최근 피드 게시글 10개 조회")
    @GetMapping("/recent")
    public ResponseEntity<ResultResponse> getRecent10Posts(@RequestParam int page) {
        final List<FeedReaderDTO> postList = feedService.getFeedPage(10, page).getContent();

        return ResponseEntity.ok(ResultResponse.of(FIND_RECENT10POSTS_SUCCESS, postList));
    }
    @ApiOperation(value = "스크랩")
    @PostMapping("/scrap")
    public ResponseEntity<ResultResponse> scrapFeed(@RequestParam Long npost_id){
        feedService.scrapFeed(npost_id);
        return ResponseEntity.ok(ResultResponse.of(BOOKMARK_POST_SUCCESS));
    }
    @ApiOperation(value = "스크랩 취소")
    @DeleteMapping("/scrap")
    public ResponseEntity<ResultResponse> unScrapFeed(@RequestParam Long npost_id) {
        feedService.unScrapFeed(npost_id);

        return ResponseEntity.ok(ResultResponse.of(UN_BOOKMARK_POST_SUCCESS));
    }
}
