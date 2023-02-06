package org.whitebox.howlook.domain.post.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.dto.PostRegisterDTO;
import org.whitebox.howlook.domain.post.dto.HashtagDTO;
import org.whitebox.howlook.domain.post.service.PostService;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;
import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.*;

@RestController
@RequestMapping("/post")
@Log4j2
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;


    @ApiOperation(value = "피드 게시글 등록")
    @PostMapping(value = "/register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> registerPost(@Valid @ModelAttribute PostRegisterDTO postRegisterDTO) {
        log.info("post POST register!");
        log.info(postRegisterDTO);

        postService.registerPOST(postRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(CREATE_POST_SUCCESS));
    }

    @ApiOperation(value = "게시글 해시태그와 댓글 함께 삭제된다. 사진은 삭제되지 않음")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<ResultResponse> deletePost(@RequestParam Long postId) {
        postService.deletepost(postId);

        return ResponseEntity.ok(ResultResponse.of(DELETE_POST_SUCCESS));
    }

    @ApiOperation(value = "게시글 id로 피드 게시글 조회")
    @GetMapping("/readbypid")
    public ResponseEntity<ResultResponse> readpostbyPID(Long postId) {
        PostReaderDTO postReaderDTO = postService.readerPID(postId);

        log.info(postReaderDTO);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS, postReaderDTO));
    }
    @ApiOperation(value = "멤버 id로 피드 게시글 모두 조회")
    @GetMapping("/readbyuid")
    public ResponseEntity<ResultResponse> readpostbyUID(String UserID) {
        List<PostReaderDTO> posts = postService.readerUID(UserID);

        log.info(posts);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS,posts));
    }
    @ApiOperation(value = "최근 피드 게시글 10개 조회")
    @GetMapping("/recent")
    public ResponseEntity<ResultResponse> getRecent10Posts(@RequestParam int page) {
        final List<PostReaderDTO> postList = postService.getpostPage(10, page).getContent();

        return ResponseEntity.ok(ResultResponse.of(FIND_RECENT10POSTS_SUCCESS, postList));
    }

    @ApiOperation(value = "좌표 기준 근처 게시글 10개 조회")
    @GetMapping("/near")
    public ResponseEntity<ResultResponse> getNear10Posts(@RequestParam int page,float latitude, float longitude) {
        final List<PostReaderDTO> postList = postService.getNearpostPage(10, page,latitude,longitude).getContent();

        return ResponseEntity.ok(ResultResponse.of(FIND_RECENT10POSTS_SUCCESS, postList));
    }
    @ApiOperation(value = "스크랩")
    @PostMapping("/scrap")
    public ResponseEntity<ResultResponse> scrappost(@RequestParam Long postId){
        postService.scrappost(postId);
        return ResponseEntity.ok(ResultResponse.of(BOOKMARK_POST_SUCCESS));
    }
    @ApiOperation(value = "스크랩 취소")
    @DeleteMapping("/scrap")
    public ResponseEntity<ResultResponse> unScrappost(@RequestParam Long postId) {
        postService.unScrappost(postId);

        return ResponseEntity.ok(ResultResponse.of(UN_BOOKMARK_POST_SUCCESS));
    }

    //posts에 값이 없으면 Error 반환해야함. 차후 설정필요
    @GetMapping("/search")
    @ApiOperation(value = "카테고리로 피드검색", notes= "카테고리(해시태그 + 사용자정보(키, 몸무게, 성별)로 피드검색함\n해시태그는 반드시 true/false중 하나 선택해야함.(비워두면 안됨)\n" +
            "page에는 표시하고 싶은 page번호 입력(0부터시작), 현재 한 페이지당 post 5개씩 출력됨.")
    public ResponseEntity<ResultResponse> searchByCategories(HashtagDTO hashtagDTO, Long heightHigh, Long heightLow, Long weightHigh, Long weightLow, char gender, int page) {
        final List<PostReaderDTO> posts = postService.searchpostByHashtag(hashtagDTO, heightHigh, heightLow, weightHigh, weightLow, gender, page, 5);

        return ResponseEntity.ok(ResultResponse.of(GET_HASHTAG_post_SUCCESS, posts));
    }

    @ApiOperation(value = "게시물 좋아요", notes = "POST 방식으로 추가")
    @PostMapping("/like")
    public ResponseEntity<ResultResponse> likepost(@RequestParam Long postId) {
        postService.likepost(postId);
        return ResponseEntity.ok(ResultResponse.of(LIKE_POST_SUCCESS));
    }

    @ApiOperation(value = "게시물 좋아요 해제", notes = "Delete 방식으로 제거")
    @DeleteMapping("/like")
    public ResponseEntity<ResultResponse> unlikepost(@RequestParam Long postId) {
        postService.unlikepost(postId);
        return ResponseEntity.ok(ResultResponse.of(UN_LIKE_POST_SUCCESS));
    }
}