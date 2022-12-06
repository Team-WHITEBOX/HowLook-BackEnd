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
import org.whitebox.howlook.global.result.ResultCode;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;
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
    public Map<String,Long> register(@Valid @RequestBody ReplyRegisterDTO replyRegisterDTO) // 댓글 등록
    {
        log.info(replyRegisterDTO);

//        if(bindingResult.hasErrors()) {
//            throw new BindException(bindingResult);
//        }

        Map<String,Long> resultMap = new HashMap<>();

        long ReplyId = replyService.register_reply(replyRegisterDTO);

        resultMap.put("ReplyId",ReplyId);

        return resultMap;
    }

    @ApiOperation(value = "Read Reply", notes = "댓글 목록 불러오기") // 특정 댓글 불러오기
    @GetMapping(value = "/{ReplyId}")
    public ReplyReadDTO getReplyDTO( @PathVariable("ReplyId") long ReplyId) {

        ReplyReadDTO replyReadDTO = replyService.read(ReplyId);

        return replyReadDTO;
    }

    @ApiOperation(value = "Delete Reply" , notes  = "DELETE 방식으로 특정 댓글 삭제") // 특정 댓글 삭제
    @DeleteMapping("/{ReplyId}")
    public Map<String,Long> remove (@PathVariable("ReplyId") Long ReplyId) {
        replyService.remove(ReplyId);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("ReplyId",ReplyId);

        return resultMap;
    }

    @ApiOperation(value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{ReplyId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(@PathVariable("ReplyId") Long ReplyId, @RequestBody ReplyDTO replyDTO) {
        replyDTO.setReplyId(ReplyId);
        replyService.modify(replyDTO);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("ReplyId",ReplyId);
        return resultMap;
    }

    @ApiOperation(value = "Replies of Feed", notes = "GET 방식으로 특정 게시물의 댓글 목록") // 한 게시물의 댓글
    @GetMapping(value = "/list/{NpostId}")
    public List<ReplyListDTO> getList(@PathVariable("NpostId") Long NpostId) {
        List<Reply> responseDTO = replyService.getListOfFeed(NpostId);
        List<ReplyListDTO> list = new ArrayList<>();

        for(int i = 0; i < responseDTO.size(); i++) {
            ReplyListDTO replyListDTO = new ReplyListDTO();
            // 댓글정보
            replyListDTO.setContents(responseDTO.get(i).getContents());
            replyListDTO.setNPostId(responseDTO.get(i).getFeed().getNPostId());
            replyListDTO.setParentId(responseDTO.get(i).getParentsId());
            replyListDTO.setReplyId(responseDTO.get(i).getReplyId());

            // 회원정보
//            replyListDTO.setNickname(responseDTO.get(i).getNickName());
//            replyListDTO.setProfilePhotoId(responseDTO.get(i).getProfilePhotoId());
            list.add(replyListDTO);
        }
        return list;
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