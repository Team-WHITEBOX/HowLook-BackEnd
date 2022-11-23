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
import org.whitebox.howlook.domain.feed.dto.ReplyDTO;
import org.whitebox.howlook.domain.feed.service.ReplyService;

import javax.validation.Valid;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> register(@Valid @RequestBody ReplyDTO replyDTO)
    {
        log.info(replyDTO);

//        if(bindingResult.hasErrors()) {
//            throw new BindException(bindingResult);
//        }

        Map<String,Long> resultMap = new HashMap<>();

        long CommendId = replyService.register(replyDTO);

        resultMap.put("CommendId",CommendId);

        return resultMap;
    }

    @ApiOperation(value = "Read Reply", notes = "댓글 목록 불러오기")
    @GetMapping(value = "/{commentId}")
    public ReplyDTO getReplyDTO( @PathVariable("commentId") long CommentId) {

        ReplyDTO replyDTO = replyService.read(CommentId);

        return replyDTO;
    }

    @ApiOperation(value = "Delete Reply" , notes  = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{CommentId}")
    public Map<String,Long> remove (@PathVariable("CommentId") long commentId) {
        replyService.remove(commentId);
    }
}