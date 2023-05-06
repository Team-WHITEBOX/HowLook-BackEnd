package org.whitebox.howlook.domain.post.controller;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.post.dto.ReplyModifyDTO;
import org.whitebox.howlook.domain.post.dto.ReplyReadDTO;
import org.whitebox.howlook.domain.post.dto.ReplyRegisterDTO;
import org.whitebox.howlook.domain.post.service.ReplyService;
import org.whitebox.howlook.global.result.ResultCode;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> register(@Valid @RequestBody ReplyRegisterDTO replyRegisterDTO)
    {
        replyService.register_reply(replyRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.CREATE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "Read Reply", notes = "특정 댓글 조회")
    @GetMapping(value = "/{ReplyId}")
    public ResponseEntity<ResultResponse> getReplyDTO(@PathVariable("ReplyId") @Positive @NotNull(message = "댓글 아이디를 입력해주세요.") long ReplyId) {
        ReplyReadDTO response = replyService.read(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_REPLY_SUCCESS,response));
    }

    @ApiOperation(value = "Delete Reply" , notes  = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{ReplyId}")
    public ResponseEntity<ResultResponse> remove(@PathVariable("ReplyId") @Positive Long ReplyId) {
        replyService.remove(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{ReplyId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> modify(@PathVariable("ReplyId") @Positive @NotNull(message = "댓글 아이디를 입력해주세요.") long replyId, @RequestBody ReplyModifyDTO replyModifyDTO) {
        replyService.modify(replyModifyDTO,replyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.MODIFY_REPLY_SUCCESS));
    }

    @ApiOperation(value = "Replies of post", notes = "GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/list/{postId}")
    public ResponseEntity<ResultResponse> getList(@PathVariable("postId") @NotNull(message = "게시글 아이디를 입력하세요.") @Positive Long postId) {
        List<ReplyReadDTO> response = replyService.getListOfPost(postId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_REPLY_IN_POST_SUCCESS,response));
    }

    @ApiOperation(value = "댓글 좋아요", notes = "POST 방식으로 추가")
    @PostMapping("/like")
    public ResponseEntity<ResultResponse> likeReply(@RequestParam @Positive @NotNull(message = "댓글 아이디를 입력하세요.") long ReplyId) {
        replyService.likeReply(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.LIKE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "댓글 좋아요 취소", notes = "Delete 방식으로 삭제")
    @DeleteMapping("/like")
    public ResponseEntity<ResultResponse> unlikeReply(@RequestParam @Positive @NotNull(message = "댓글 아이디를 입력하세요.") long ReplyId) {
        replyService.unlikeReply(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.UNLIKE_COMMENT_SUCCESS));
    }
}