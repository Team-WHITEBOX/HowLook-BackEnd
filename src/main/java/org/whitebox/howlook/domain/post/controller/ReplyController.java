package org.whitebox.howlook.domain.post.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.post.dto.*;
import org.whitebox.howlook.domain.post.entity.Reply;
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
    @ApiOperation(value = "Replies POST", notes = "POST 방식?���? ?���? ?���?")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> register(@Valid @RequestBody ReplyRegisterDTO replyRegisterDTO) // ?���? ?���?
    {
        replyService.register_reply(replyRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.CREATE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "Read Reply", notes = "?��?�� ?���? 조회") // ?��?�� ?���? 불러?���?
    @GetMapping(value = "/{ReplyId}")
    public ResponseEntity<ResultResponse> getReplyDTO(@PathVariable("ReplyId") @Positive @NotNull(message = "?���? ?��?��?���? ?��?��?��?��?��.") long ReplyId) {
        ReplyReadDTO response = replyService.read(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_REPLY_SUCCESS,response));
    }

    @ApiOperation(value = "Delete Reply" , notes  = "DELETE 방식?���? ?��?�� ?���? ?��?��") // ?��?�� ?���? ?��?��
    @DeleteMapping("/{ReplyId}")
    public ResponseEntity<ResultResponse> remove(@PathVariable("ReplyId") @Positive Long ReplyId) {
        replyService.remove(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "Modify Reply", notes = "PUT 방식?���? ?��?�� ?���? ?��?��")
    @PutMapping(value = "/{ReplyId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> modify(@PathVariable("ReplyId") @Positive @NotNull(message = "?���? ?��?��?���? ?��?��?��?��?��.") long replyId, @RequestBody ReplyModifyDTO replyModifyDTO) {
        replyService.modify(replyModifyDTO,replyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.MODIFY_REPLY_SUCCESS));
    }

    @ApiOperation(value = "Replies of post", notes = "GET 방식?���? ?��?�� 게시물의 ?���? 목록") // ?�� 게시물의 ?���?
    @GetMapping(value = "/list/{postId}")
    public ResponseEntity<ResultResponse> getList(@PathVariable("postId") @NotNull(message = "게시�? ?��?��?���? ?��?��?��?��?��.") @Positive Long postId) {
        List<ReplyReadDTO> response = replyService.getListOfpost(postId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_REPLY_IN_POST_SUCCESS,response));
    }

    @ApiOperation(value = "?���? 좋아?��", notes = "POST 방식?���? 추�??")
    @PostMapping("/like")
    public ResponseEntity<ResultResponse> likeReply(@RequestParam @Positive @NotNull(message = "?���? ?��?��?���? ?��?��?��?��?��.") long ReplyId) {
        replyService.likeReply(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.LIKE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "?���? 좋아?�� 취소", notes = "Delete 방식?���? ?��?��")
    @DeleteMapping("/like")
    public ResponseEntity<ResultResponse> unlikeReply(@RequestParam @Positive @NotNull(message = "?���? ?��?��?���? ?��?��?��?��?��.") long ReplyId) {
        replyService.unlikeReply(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.UNLIKE_COMMENT_SUCCESS));
    }
}