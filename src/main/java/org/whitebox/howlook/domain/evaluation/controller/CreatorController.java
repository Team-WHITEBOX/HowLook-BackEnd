package org.whitebox.howlook.domain.evaluation.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.evaluation.dto.*;
import org.whitebox.howlook.domain.evaluation.service.CreatorEvalService;
import org.whitebox.howlook.global.result.ResultResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.*;

@RestController
@RequestMapping("/CreatorEval")
@Log4j2
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorEvalService creatorEvalService;

    @ApiOperation(value = "크리에이터 평가글 등록하기")
    @PostMapping(value = "/register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> registerPosts(@Valid @ModelAttribute CreatorEvalRegisterDTO creatorEvalRegisterDTO) {
        creatorEvalService.registerCreatorEval(creatorEvalRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(CREATOR_EVAL_POST_SUCCESS));
    }
    @ApiOperation(value = "크리에이터 평가 글 번호로 글 불러오기 : 평가 글 ID")
    @GetMapping("/readByCreatorId")
    public ResponseEntity<ResultResponse> readCreatorEval(@NotNull(message = "p") Long creatorEvalId) {
        CreatorEvalReadDTO creatorEvalReadDTO = creatorEvalService.readByCreatorEvalId(creatorEvalId);
        return ResponseEntity.ok(ResultResponse.of(GET_CREATOR_EVAL,creatorEvalReadDTO));
    }

    @ApiOperation(value = "다음 평가 글 불러오기 : 파라메터 없음")
    @GetMapping("/readNextCreatorEval")
    public ResponseEntity<ResultResponse> readNextCreatorEval()
    {
        final CreatorEvalPageDTO creatorEvalPageDTO = creatorEvalService.getCreatorEvalWithHasMore(0,2);
        if(creatorEvalPageDTO == null)
        {
            return ResponseEntity.ok(ResultResponse.of(EVAL_SEARCH_FAIL));
        }

        return ResponseEntity.ok(ResultResponse.of(EVAL_SEARCH_SUCCESS,creatorEvalPageDTO));
    }

    @ApiOperation(value = "유저 아이디로 평가 글 불러오기 : 유저 아이디")
    @GetMapping("/readByUserId")
    public ResponseEntity<ResultResponse> readByUserId(@NotNull(message = "userID는 필수입니다.") String userId) {
        List<CreatorEvalReadDTO> creatorEvalReadDTOS = creatorEvalService.getListOfUId(userId);
        return ResponseEntity.ok(ResultResponse.of(GET_CREATOR_EVAL_LIST_SUCCESS,creatorEvalReadDTOS));
    }

    @ApiOperation(value = "현재 유저가 크리에이터인지 아닌지 불러오기 : 파라메터 없음")
    @GetMapping("/isCreator")
    public ResponseEntity<ResultResponse> readByUserId() {
        Boolean isCreator = creatorEvalService.checkIAMCreator();
        return ResponseEntity.ok(ResultResponse.of(CREATOR_SEARCH_SUCCESS,isCreator));
    }
}
