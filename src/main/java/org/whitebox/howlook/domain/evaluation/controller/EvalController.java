package org.whitebox.howlook.domain.evaluation.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.whitebox.howlook.domain.evaluation.dto.EvalPageDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalRegisterDTO;
import org.whitebox.howlook.domain.evaluation.service.EvalService;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.*;

@RestController
@RequestMapping("/eval")
@Log4j2
@RequiredArgsConstructor
public class EvalController {
    private final EvalService evalService;


    @ApiOperation(value = "평가글 등록하기 : 사진 한장")
    @PostMapping(value = "/registerPost",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> registerPosts(@Valid @ModelAttribute EvalRegisterDTO evalRegisterDTO) {
        evalService.register(evalRegisterDTO);

        return ResponseEntity.ok(ResultResponse.of(EVAL_REGISTER_SUCCESS, true));
    }

    // 평가 게시글 아이디로 평가 글 정보 가져오기
    @ApiOperation(value = "평가 글 번호로 글 불러오기 : 평가 글 ID")
    @GetMapping("/readByPostId")
    public ResponseEntity<ResultResponse> readEval(@NotNull(message = "postId는 필수입니다.") Long postId) {
        EvalReaderDTO evalReaderDTO = evalService.reader(postId);

        return ResponseEntity.ok(ResultResponse.of(EVAL_SEARCH_SUCCESS,evalReaderDTO));
    }

    // 평가안한 평가 글 전부 가져오기
    @ApiOperation(value = "평가 안한 글 불러오기 : 파라메터 없음")
    @GetMapping("/readAnyEval")
    public ResponseEntity<ResultResponse> readAnyEval() {
        List<EvalReaderDTO> evalReaderDTOS = evalService.readAll();

        if(evalReaderDTOS.size() == 0)
        {
            return ResponseEntity.ok(ResultResponse.of(EVAL_SEARCH_FAIL));
        }

        return ResponseEntity.ok(ResultResponse.of(EVAL_SEARCH_SUCCESS,evalReaderDTOS));
    }

    // 다음 평가 글 불러오기
    @ApiOperation(value = "다음 평가 글 불러오기 : 파라메터 없음")
    @GetMapping("/readNextEval")
    public ResponseEntity<ResultResponse> readNextEval()
    {
        final EvalPageDTO evalPageDTO = evalService.getEvalWithHasMore(0,2);

        if(evalPageDTO == null) // || evalPage.getPostId() == null)
        {
            return ResponseEntity.ok(ResultResponse.of(EVAL_SEARCH_FAIL));
        }

        return ResponseEntity.ok(ResultResponse.of(EVAL_SEARCH_SUCCESS,evalPageDTO));
    }

    @ApiOperation(value = "유저 아이디로 평가 글 불러오기 : 유저 아이디")
    @GetMapping("/readByUserId")
    public ResponseEntity<ResultResponse> readByUserId(@NotNull(message = "userID는 필수입니다.") String userID) {
        List<EvalReaderDTO> evalReaderDTOS = evalService.readerUID(userID);

        return ResponseEntity.ok(ResultResponse.of(EVAL_SEARCH_SUCCESS,evalReaderDTOS));
    }
    
}
