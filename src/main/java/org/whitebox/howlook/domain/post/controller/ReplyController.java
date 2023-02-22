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
    @ApiOperation(value = "Replies POST", notes = "POST λ°©μ?Όλ‘? ?κΈ? ?±λ‘?")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> register(@Valid @RequestBody ReplyRegisterDTO replyRegisterDTO) // ?κΈ? ?±λ‘?
    {
        replyService.register_reply(replyRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.CREATE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "Read Reply", notes = "?Ή?  ?κΈ? μ‘°ν") // ?Ή?  ?κΈ? λΆλ¬?€κΈ?
    @GetMapping(value = "/{ReplyId}")
    public ResponseEntity<ResultResponse> getReplyDTO(@PathVariable("ReplyId") @Positive @NotNull(message = "?κΈ? ??΄?λ₯? ?? ₯??Έ?.") long ReplyId) {
        ReplyReadDTO response = replyService.read(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_REPLY_SUCCESS,response));
    }

    @ApiOperation(value = "Delete Reply" , notes  = "DELETE λ°©μ?Όλ‘? ?Ή?  ?κΈ? ?­? ") // ?Ή?  ?κΈ? ?­? 
    @DeleteMapping("/{ReplyId}")
    public ResponseEntity<ResultResponse> remove(@PathVariable("ReplyId") @Positive Long ReplyId) {
        replyService.remove(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "Modify Reply", notes = "PUT λ°©μ?Όλ‘? ?Ή?  ?κΈ? ?? ")
    @PutMapping(value = "/{ReplyId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> modify(@PathVariable("ReplyId") @Positive @NotNull(message = "?κΈ? ??΄?λ₯? ?? ₯??Έ?.") long replyId, @RequestBody ReplyModifyDTO replyModifyDTO) {
        replyService.modify(replyModifyDTO,replyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.MODIFY_REPLY_SUCCESS));
    }

    @ApiOperation(value = "Replies of post", notes = "GET λ°©μ?Όλ‘? ?Ή?  κ²μλ¬Όμ ?κΈ? λͺ©λ‘") // ? κ²μλ¬Όμ ?κΈ?
    @GetMapping(value = "/list/{postId}")
    public ResponseEntity<ResultResponse> getList(@PathVariable("postId") @NotNull(message = "κ²μκΈ? ??΄?λ₯? ?? ₯??Έ?.") @Positive Long postId) {
        List<ReplyReadDTO> response = replyService.getListOfpost(postId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_REPLY_IN_POST_SUCCESS,response));
    }

    @ApiOperation(value = "?κΈ? μ’μ?", notes = "POST λ°©μ?Όλ‘? μΆκ??")
    @PostMapping("/like")
    public ResponseEntity<ResultResponse> likeReply(@RequestParam @Positive @NotNull(message = "?κΈ? ??΄?λ₯? ?? ₯??Έ?.") long ReplyId) {
        replyService.likeReply(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.LIKE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "?κΈ? μ’μ? μ·¨μ", notes = "Delete λ°©μ?Όλ‘? ?­? ")
    @DeleteMapping("/like")
    public ResponseEntity<ResultResponse> unlikeReply(@RequestParam @Positive @NotNull(message = "?κΈ? ??΄?λ₯? ?? ₯??Έ?.") long ReplyId) {
        replyService.unlikeReply(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.UNLIKE_COMMENT_SUCCESS));
    }
}