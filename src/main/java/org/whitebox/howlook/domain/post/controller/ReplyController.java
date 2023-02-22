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
    @ApiOperation(value = "Replies POST", notes = "POST ë°©ì‹?œ¼ë¡? ?Œ“ê¸? ?“±ë¡?")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> register(@Valid @RequestBody ReplyRegisterDTO replyRegisterDTO) // ?Œ“ê¸? ?“±ë¡?
    {
        replyService.register_reply(replyRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.CREATE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "Read Reply", notes = "?Š¹? • ?Œ“ê¸? ì¡°íšŒ") // ?Š¹? • ?Œ“ê¸? ë¶ˆëŸ¬?˜¤ê¸?
    @GetMapping(value = "/{ReplyId}")
    public ResponseEntity<ResultResponse> getReplyDTO(@PathVariable("ReplyId") @Positive @NotNull(message = "?Œ“ê¸? ?•„?´?””ë¥? ?…? ¥?•˜?„¸?š”.") long ReplyId) {
        ReplyReadDTO response = replyService.read(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_REPLY_SUCCESS,response));
    }

    @ApiOperation(value = "Delete Reply" , notes  = "DELETE ë°©ì‹?œ¼ë¡? ?Š¹? • ?Œ“ê¸? ?‚­? œ") // ?Š¹? • ?Œ“ê¸? ?‚­? œ
    @DeleteMapping("/{ReplyId}")
    public ResponseEntity<ResultResponse> remove(@PathVariable("ReplyId") @Positive Long ReplyId) {
        replyService.remove(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "Modify Reply", notes = "PUT ë°©ì‹?œ¼ë¡? ?Š¹? • ?Œ“ê¸? ?ˆ˜? •")
    @PutMapping(value = "/{ReplyId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> modify(@PathVariable("ReplyId") @Positive @NotNull(message = "?Œ“ê¸? ?•„?´?””ë¥? ?…? ¥?•˜?„¸?š”.") long replyId, @RequestBody ReplyModifyDTO replyModifyDTO) {
        replyService.modify(replyModifyDTO,replyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.MODIFY_REPLY_SUCCESS));
    }

    @ApiOperation(value = "Replies of post", notes = "GET ë°©ì‹?œ¼ë¡? ?Š¹? • ê²Œì‹œë¬¼ì˜ ?Œ“ê¸? ëª©ë¡") // ?•œ ê²Œì‹œë¬¼ì˜ ?Œ“ê¸?
    @GetMapping(value = "/list/{postId}")
    public ResponseEntity<ResultResponse> getList(@PathVariable("postId") @NotNull(message = "ê²Œì‹œê¸? ?•„?´?””ë¥? ?…? ¥?•˜?„¸?š”.") @Positive Long postId) {
        List<ReplyReadDTO> response = replyService.getListOfpost(postId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_REPLY_IN_POST_SUCCESS,response));
    }

    @ApiOperation(value = "?Œ“ê¸? ì¢‹ì•„?š”", notes = "POST ë°©ì‹?œ¼ë¡? ì¶”ê??")
    @PostMapping("/like")
    public ResponseEntity<ResultResponse> likeReply(@RequestParam @Positive @NotNull(message = "?Œ“ê¸? ?•„?´?””ë¥? ?…? ¥?•˜?„¸?š”.") long ReplyId) {
        replyService.likeReply(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.LIKE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "?Œ“ê¸? ì¢‹ì•„?š” ì·¨ì†Œ", notes = "Delete ë°©ì‹?œ¼ë¡? ?‚­? œ")
    @DeleteMapping("/like")
    public ResponseEntity<ResultResponse> unlikeReply(@RequestParam @Positive @NotNull(message = "?Œ“ê¸? ?•„?´?””ë¥? ?…? ¥?•˜?„¸?š”.") long ReplyId) {
        replyService.unlikeReply(ReplyId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.UNLIKE_COMMENT_SUCCESS));
    }
}