package org.whitebox.howlook.domain.evaluation.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whitebox.howlook.domain.evaluation.dto.CreatorReplyDTO;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

import static org.whitebox.howlook.global.result.ResultCode.CREATOR_EVAL_REPLY_SUCCESS;

@RestController
@RequestMapping("/CreatorReply")
@Log4j2
@RequiredArgsConstructor
public class CreatorReplyController {


    @ApiOperation(value = "크리에이터 평가 댓글 등록하기")
    @PostMapping(value = "/evalCreator")
    public ResponseEntity<ResultResponse> evalCreator(@Valid @ModelAttribute CreatorReplyDTO creatorReplyDTO) {
        // 글 번호에 점수와 리뷰 등록함

        return ResponseEntity.ok(ResultResponse.of(CREATOR_EVAL_REPLY_SUCCESS));
    }

}
