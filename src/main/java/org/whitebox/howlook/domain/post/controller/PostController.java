package org.whitebox.howlook.domain.post.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.dto.PostRegisterDTO;
import org.whitebox.howlook.domain.post.dto.SearchCategoryDTO;
import org.whitebox.howlook.domain.post.service.PostService;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.*;

@RestController
@RequestMapping("/post")
@Log4j2
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @ApiOperation(value = "피드 게시글 등록")
    @PostMapping(value = "/regist",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> registerPost(@Valid @ModelAttribute PostRegisterDTO postRegisterDTO) {
        log.info("post POST register!");
        log.info(postRegisterDTO);

        return ResponseEntity.ok(ResultResponse.of(CREATE_POST_SUCCESS));
    }

    @ApiOperation(value = "게시글 해시태그와 댓글 함께 삭제된다. 사진은 삭제되지 않음")
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<ResultResponse> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);

        return ResponseEntity.ok(ResultResponse.of(DELETE_POST_SUCCESS));
    }

    @ApiOperation(value = "게시글 id로 피드 게시글 조회")
    @GetMapping("/readbypid")
    public ResponseEntity<ResultResponse> readPostByPID(@NotNull(message = "postId는 필수입니다.") @Positive Long postId) {
        PostReaderDTO postReaderDTO = postService.readerPID(postId);

        log.info(postReaderDTO);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_BY_ID_SUCCESS, postReaderDTO));
    }
    @ApiOperation(value = "멤버 id로 피드 게시글 모두 조회")
    @GetMapping("/readbyuid")
    public ResponseEntity<ResultResponse> readPostByUID(String UserID) {
        List<PostReaderDTO> posts = postService.readerUID(UserID);

        log.info(posts);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_BY_MEMBER_ID_SUCCESS,posts));
    }
    @ApiOperation(value = "최근 피드 게시글 10개 조회")
    @GetMapping("/recent")
    public ResponseEntity<ResultResponse> getRecent10Posts(@RequestParam @NotNull @Positive int page) {
        final List<PostReaderDTO> postList = postService.getPostPage(10, page).getContent();

        return ResponseEntity.ok(ResultResponse.of(FIND_RECENT10POSTS_SUCCESS, postList));
    }

    @ApiOperation(value = "좌표 기준 근처 게시글 10개 조회")
    @GetMapping("/near")
    public ResponseEntity<ResultResponse> getNear10Posts(@RequestParam @NotNull int page, float latitude, float longitude) {
        final List<PostReaderDTO> postList = postService.getNearPostPage(10, page,latitude,longitude).getContent();

        return ResponseEntity.ok(ResultResponse.of(FIND_RECENT10POSTS_SUCCESS, postList));
    }
    @ApiOperation(value = "스크랩")
    @PostMapping("/scrap")
    public ResponseEntity<ResultResponse> scrapPost(@RequestParam @NotNull(message = "PostId는 필수입니다.") @Positive Long postId){
        postService.scrapPost(postId);
        return ResponseEntity.ok(ResultResponse.of(BOOKMARK_POST_SUCCESS));
    }
    @ApiOperation(value = "스크랩 취소")
    @DeleteMapping("/scrap")
    public ResponseEntity<ResultResponse> unScrapPost(@RequestParam @NotNull(message = "PostId는 필수입니다.") @Positive Long postId) {
        postService.unScrapPost(postId);

        return ResponseEntity.ok(ResultResponse.of(UN_BOOKMARK_POST_SUCCESS));
    }

    //posts에 값이 없으면 Error 반환해야함. 차후 설정필요
    @GetMapping("/search")
    @ApiOperation(value = "카테고리로 피드검색", notes= "카테고리(해시태그 + 사용자정보(키, 몸무게, 성별)로 피드검색함\n해시태그는 반드시 true/false중 하나 선택해야함.(비워두면 안됨)\n" +
            "page에는 표시하고 싶은 page번호 입력(0부터시작), 현재 한 페이지당 post 5개씩 출력됨.")
    public ResponseEntity<ResultResponse> searchByCategories(SearchCategoryDTO searchCategoryDTO) {
        final List<PostReaderDTO> posts = postService.searchPostByHashtag(searchCategoryDTO);

        return ResponseEntity.ok(ResultResponse.of(GET_HASHTAG_POST_SUCCESS, posts));
    }

    @ApiOperation(value = "게시물 좋아요", notes = "POST 방식으로 추가")
    @PostMapping("/like")
    public ResponseEntity<ResultResponse> likePost(@RequestParam @NotNull(message = "PostId는 필수입니다.") @Positive Long postId) {
        postService.likePost(postId);
        return ResponseEntity.ok(ResultResponse.of(LIKE_POST_SUCCESS));
    }

    @ApiOperation(value = "게시물 좋아요 해제", notes = "Delete 방식으로 제거")
    @DeleteMapping("/like")
    public ResponseEntity<ResultResponse> unlikePost(@RequestParam @NotNull(message = "PostId는 필수입니다.") @Positive Long postId) {
        postService.unlikePost(postId);
        return ResponseEntity.ok(ResultResponse.of(UN_LIKE_POST_SUCCESS));
    }
}