package org.whitebox.howlook.domain.evaluation.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalModifyDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalReadDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalRegisterDTO;
import org.whitebox.howlook.domain.evaluation.service.CreatorEvalService;
import org.whitebox.howlook.domain.post.dto.ReplyModifyDTO;
import org.whitebox.howlook.domain.post.dto.ReplyReadDTO;
import org.whitebox.howlook.global.result.ResultCode;
import org.whitebox.howlook.global.result.ResultResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/Creator")
@Log4j2
@RequiredArgsConstructor
public class creatorController {
    private final CreatorEvalService creatorEvalService;
    @ApiOperation(value = "CreatorEval POST", notes = "크리에이터 평가글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE) // 크리에이터 평가글 등록
    public ResponseEntity<ResultResponse> register(@Valid @RequestBody CreatorEvalRegisterDTO creatorEvalRegisterDTO)
    {
        creatorEvalService.registerCreatorEval(creatorEvalRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.CREATOR_EVAL_REGISTER_SUCCESS));
    }

    @ApiOperation(value = "Read CreatorEval", notes = "크리에이터 평가글 아이디로 특정 평가글 조회")
    @GetMapping(value = "/{creatorEvalId}")
    public ResponseEntity<ResultResponse> getEvalId(@PathVariable("creatorEvalId") @Positive @NotNull(message = "크리에이터 평가글의 아이디를 입력해주세요.")
                                                    Long creatorEvalId) {
        CreatorEvalReadDTO response = creatorEvalService.readCreatorEval(creatorEvalId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_CREATOR_EVAL,response));
    }

    @ApiOperation(value = "Delete CreatorEval" , notes  = "DELETE 방식으로 특정 크리에이터 평가글 삭제")
    @DeleteMapping("/{creatorEvalId}")
    public ResponseEntity<ResultResponse> remove(@PathVariable("creatorEvalId") @Positive Long creatorEvalId) {
        creatorEvalService.remove(creatorEvalId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_CREATOR_EVAl_SUCCESS));
    }

    @ApiOperation(value = "Modify CreatorEval", notes = "PUT 방식으로 특정 크리에이터 평가글 수정")
    @PutMapping(value = "/modify", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> modify(@Valid @RequestBody CreatorEvalModifyDTO creatorEvalModifyDTO) {
        creatorEvalService.modify(creatorEvalModifyDTO);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.MODIFY_CREATOR_EVAL_SUCCESS));
    }

    @ApiOperation(value = "CreatorEvals of Eval", notes = "GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/list/{EvalId}")
    public ResponseEntity<ResultResponse> getList(@PathVariable("EvalId") @NotNull(message = "평가글 아이디를 입력하세요.") @Positive Long EvalId) {
        List<CreatorEvalReadDTO> response = creatorEvalService.getListOfEval(EvalId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_CREATOR_EVAL_LIST_SUCCESS,response));
    }
}
