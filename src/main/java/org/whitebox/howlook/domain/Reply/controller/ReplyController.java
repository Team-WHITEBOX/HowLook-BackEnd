package org.whitebox.howlook.domain.Reply.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.Reply.dto.ReplyDTO;
import org.whitebox.howlook.domain.Reply.service.ReplyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor // 의존성 주입
public class ReplyController {
    private final ReplyService replyService;

    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록") // 댓글 등록
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Integer> register( // 등록
            @Valid @RequestBody ReplyDTO replyDTO,
            BindingResult bindingResult) throws BindException {

        log.info(replyDTO);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Integer> resultMap = new HashMap<>();

        int rno = replyService.register(replyDTO);

        resultMap.put("rno", rno);

        return resultMap;
    }

//    @ApiOperation(value = "Replies of Board", notes = "GET 방식으로 특정게시물의 댓글 목록") // board 있어야 가능.
//    @GetMapping(value = "/list/{bno}")
//    public PageResponseDTO

    @ApiOperation(value = "Read Reply", notes = "GET 방식으로 특정 댓글 조회") // 댓글 조회
    @GetMapping("/{commendId}")
    public ReplyDTO getReplyDTO(@PathVariable("commendId") int CommendId) {

        ReplyDTO replyDTO = replyService.read(CommendId);

        return replyDTO;
    }

    @ApiOperation(value = "Delete Reply", notes = "DELETE 방식으로 특정 댓글 삭제") // 댓글 삭제
    @DeleteMapping("/{commendId}")
    public Map<String,Integer> remove(@PathVariable("commendId") int CommendId) {

        replyService.remove(CommendId);

        Map<String, Integer> resultMap = new HashMap<>();

        resultMap.put("commendId",CommendId);

        return resultMap;
    }

    @ApiOperation(value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{commendId}", consumes = MediaType.APPLICATION_JSON_VALUE )
    public Map<String,Integer> remove( @PathVariable("commendId") int CommendId,
                                    @RequestBody ReplyDTO replyDTO ){

        replyDTO.setRno(CommendId); //번호를 일치시킴

        replyService.modify(replyDTO);

        Map<String, Integer> resultMap = new HashMap<>();

        resultMap.put("CommendId", CommendId);

        return resultMap;
    }
}