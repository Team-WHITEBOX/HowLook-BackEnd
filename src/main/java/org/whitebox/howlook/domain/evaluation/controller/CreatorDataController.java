package org.whitebox.howlook.domain.evaluation.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.whitebox.howlook.global.result.ResultResponse;
import javax.validation.constraints.NotNull;

import static org.whitebox.howlook.global.result.ResultCode.GET_EVAL_REPLY_SUCCESS;

public class CreatorDataController {
    @ApiOperation(value = "평가 글 아이디로 달린 평가 정보 가져오기 : 평가 글 아이디")
    @GetMapping("/getReplyData")
    public ResponseEntity<ResultResponse> getReplyData(@NotNull(message = "postId는 필수입니다.") Long postId) {
        // 아이디로 게시글에 달린 크리에이터의 평가 리스트를 가져옴. CreatorDataDTO

        return ResponseEntity.ok(ResultResponse.of(GET_EVAL_REPLY_SUCCESS));
    }
}
