package org.whitebox.howlook.domain.post.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.post.dto.*;
import org.whitebox.howlook.domain.post.service.ReplyService;
import org.whitebox.howlook.global.result.ResultCode;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> register(@Valid @RequestBody ReplyRegisterDTO replyRegisterDTO) // 댓글 등록
    {
        replyService.register_reply(replyRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.CREATE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "Read Reply", notes = "특정 댓글 조회") // 특정 댓글 불러오기
    @GetMapping(value = "/{ReplyId}")
    public ResponseEntity<ResultResponse> getReplyDTO( @PathVariable("ReplyId") long ReplyId) {
        ReplyReadDTO response = replyService.read(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_REPLY_SUCCESS,response));
    }

    @ApiOperation(value = "Delete Reply" , notes  = "DELETE 방식으로 특정 댓글 삭제") // 특정 댓글 삭제
    @DeleteMapping("/{ReplyId}")
    public ResponseEntity<ResultResponse> remove(@PathVariable("ReplyId") Long ReplyId) {
        replyService.remove(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{ReplyId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> modify(@PathVariable("ReplyId") Long ReplyId, @RequestBody ReplyDTO replyDTO) {
        replyDTO.setReplyId(ReplyId);
        replyService.modify(replyDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.MODIFY_REPLY_SUCCESS));
    }

    @ApiOperation(value = "Replies of post", notes = "GET 방식으로 특정 게시물의 댓글 목록") // 한 게시물의 댓글
    @GetMapping(value = "/list/{postId}")
    public ResponseEntity<ResultResponse> getList(@PathVariable("postId") Long postId) {
        List<ReplyReadDTO> response = replyService.getListOfpost(postId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_REPLY_IN_post_SUCESS,response));
    }

    @ApiOperation(value = "댓글 좋아요", notes = "POST 방식으로 추가")
    @PostMapping("/like")
    public ResponseEntity<ResultResponse> likeReply(@RequestParam Long ReplyId) {
        replyService.likeReply(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.LIKE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "댓글 좋아요 취소", notes = "Delete 방식으로 삭제")
    @DeleteMapping("/like")
    public ResponseEntity<ResultResponse> unlikeReply(@RequestParam Long ReplyId) {
        replyService.unlikeReply(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.UNLIKE_COMMENT_SUCCESS));
    }
}