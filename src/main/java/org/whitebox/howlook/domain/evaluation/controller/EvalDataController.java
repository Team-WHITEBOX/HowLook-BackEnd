package org.whitebox.howlook.domain.evaluation.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whitebox.howlook.domain.evaluation.dto.EvalDataDTO;
import org.whitebox.howlook.domain.evaluation.service.EvalReplyService;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.constraints.NotNull;

import static org.whitebox.howlook.global.result.ResultCode.*;

@RestController
@RequestMapping("/eval")
@Log4j2
@RequiredArgsConstructor
public class EvalDataController {
    private final EvalReplyService evalReplyService;

    @ApiOperation(value = "평가 글 아이디로 달린 평가 정보 가져오기 : 평가 글 아이디")
    @GetMapping("/getReplyData")
    public ResponseEntity<ResultResponse> getReplyData(@NotNull(message = "postId는 필수입니다.") Long postId) {
        EvalDataDTO evalDataDTO = evalReplyService.ReadDateByPostId(postId);
        log.info(evalDataDTO);

        return ResponseEntity.ok(ResultResponse.of(EVAL_SEARCH_SUCCESS, evalDataDTO));
    }
}
