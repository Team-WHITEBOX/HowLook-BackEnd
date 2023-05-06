package org.whitebox.howlook.domain.creator.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whitebox.howlook.domain.creator.dto.CreatorEvalRegistorDTO;
import org.whitebox.howlook.domain.creator.service.CreatorEvalService;
import org.whitebox.howlook.global.result.ResultCode;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/Creator")
@Log4j2
@RequiredArgsConstructor
public class creatorController {
    private final CreatorEvalService creatorEvalService;

    @ApiOperation(value = "CreatorEval POST", notes = "POST 방식으로 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> register(@Valid @RequestBody CreatorEvalRegistorDTO creatorEvalRegistorDTO)
    {
        creatorEvalService.registerCreatorEval(creatorEvalRegistorDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.CREATOR_EVAL_REGISTER_SUCCESS));
    }
}
