package org.whitebox.howlook.domain.creator.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.creator.dto.CreatorEvalRegisterDTO;
import org.whitebox.howlook.domain.creator.service.CreatorEvalService;
import org.whitebox.howlook.global.result.ResultCode;
import org.whitebox.howlook.global.result.ResultResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/Creator")
@Log4j2
@RequiredArgsConstructor
public class creatorController {
    private final CreatorEvalService creatorEvalService;
    @ApiOperation(value = "CreatorEval POST", notes = "POST 방식으로 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE) // 크리에이터 평가글 등록
    public ResponseEntity<ResultResponse> register(@Valid @RequestBody CreatorEvalRegisterDTO creatorEvalRegisterDTO)
    {
        creatorEvalService.registerCreatorEval(creatorEvalRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.CREATOR_EVAL_REGISTER_SUCCESS));
    }

//    @ApiOperation(value = "Read CreatorEval", notes = "특정 평가글 조회")
//    @GetMapping(value = "/{EvalId}")
//    public ResponseEntity<ResultResponse> getEvalId(@PathVariable("EvalId") @Positive @NotNull(message = "평가글의 아이디를 입력해주세요.")
//                                                    long EvalId) {
//
//    }

}
