package org.whitebox.howlook.domain.feed.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.feed.dto.*;
import org.whitebox.howlook.domain.feed.entity.Reply;
import org.whitebox.howlook.domain.feed.service.FeedService;
import org.whitebox.howlook.domain.feed.service.ReplyService;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.EntityAlreadyExistException;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.result.ResultCode;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;
import javax.xml.transform.Result;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        log.info(replyRegisterDTO);

//        if(bindingResult.hasErrors()) {
//            throw new BindException(bindingResult);
//        }
        replyService.register_reply(replyRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.CREATE_COMMENT_SUCCESS));
    }

    @ApiOperation(value = "Read Reply", notes = "댓글 목록 불러오기") // 특정 댓글 불러오기
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

    @ApiOperation(value = "Replies of Feed", notes = "GET 방식으로 특정 게시물의 댓글 목록") // 한 게시물의 댓글
    @GetMapping(value = "/list/{NpostId}")
    public ResponseEntity<ResultResponse> getList(@PathVariable("NpostId") Long NpostId) {
        List<ReplyReadDTO> response = replyService.getListOfFeed(NpostId);

        if(response.size() == 0) {
            throw new EntityNotFoundException(ErrorCode.COMMENT_NOT_FOUND);
        }

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_REPLY_IN_FEED_SUCESS,response));
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