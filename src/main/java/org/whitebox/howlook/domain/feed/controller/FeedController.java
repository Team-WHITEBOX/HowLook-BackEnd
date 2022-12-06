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
import org.whitebox.howlook.domain.member.service.MemberService;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.ErrorResponse;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;
import javax.xml.transform.Result;

import java.util.List;
import java.util.Optional;

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

    //게시글 삭제하는 API
    @DeleteMapping(value = "/delete")
    public ResponseEntity<ResultResponse> deletePost(@RequestParam Long npost_id) {
        feedService.deleteFeed(npost_id);

        return ResponseEntity.ok(ResultResponse.of(DELETE_POST_SUCCESS));
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

    @GetMapping("/near")
    public ResponseEntity<ResultResponse> getNear10Posts(@RequestParam int page,float latitude, float longitude) {
        final List<FeedReaderDTO> postList = feedService.getNearFeedPage(2, page,latitude,longitude).getContent();

        return ResponseEntity.ok(ResultResponse.of(FIND_RECENT10POSTS_SUCCESS, postList));
    }

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

    //feeds에 값이 없으면 Error 반환해야함. 차후 설정필요
    @GetMapping("/search")
    @ApiOperation(value = "카테고리로 피드검색", notes= "카테고리(해시태그 + 사용자정보(키, 몸무게, 성별)로 피드검색함\n해시태그는 반드시 true/false중 하나 선택해야함.(비워두면 안됨)\n" +
            "page에는 표시하고 싶은 page번호 입력(0부터시작), 현재 한 페이지당 post 5개씩 출력됨.")
    public ResponseEntity<ResultResponse> searchByCategories(HashtagDTO hashtagDTO, Long heightHigh, Long heightLow, Long weightHigh, Long weightLow, char gender, int page) {
        List<FeedReaderDTO> feeds = feedService.searchFeedByHashtag(hashtagDTO, heightHigh, heightLow, weightHigh, weightLow, gender, page, 5);

        return ResponseEntity.ok(ResultResponse.of(GET_HASHTAG_FEED_SUCCESS, feeds));
    }

    @ApiOperation(value = "게시물 좋아요", notes = "POST 방식으로 추가")
    @PostMapping("/like")
    public ResponseEntity<ResultResponse> likeFeed(@RequestParam Long NPostId) {
        feedService.likeFeed(NPostId);
        return ResponseEntity.ok(ResultResponse.of(LIKE_POST_SUCCESS));
    }

    @ApiOperation(value = "게시물 좋아요 해제", notes = "Delete 방식으로 제거")
    @DeleteMapping("/like")
    public ResponseEntity<ResultResponse> unlikeFeed(@RequestParam Long NPostId) {
        feedService.unlikeFeed(NPostId);
        return ResponseEntity.ok(ResultResponse.of(UN_LIKE_POST_SUCCESS));
    }
}
