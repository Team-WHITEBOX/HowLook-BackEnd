package org.whitebox.howlook.domain.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalRegisterDTO;
import org.whitebox.howlook.domain.evaluation.service.EvalService;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

import java.util.List;

import static org.whitebox.howlook.global.result.ResultCode.*;

@RestController
@RequestMapping("/eval")
@Log4j2
@RequiredArgsConstructor
public class EvalController {
    private final EvalService evalService;

    @PostMapping(value = "/register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> registerEval(@Valid @ModelAttribute EvalRegisterDTO evalRegisterDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            log.info("has errors..");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return ResponseEntity.ok(ResultResponse.of(CREATE_POST_FAIL, false));
        }

        evalService.register(evalRegisterDTO);

        return ResponseEntity.ok(ResultResponse.of(EVAL_REGISTER_SUCCESS, true));
    }

    // 평가 게시글 아이디로 게시글 정보 가져오기
    @GetMapping("/readbypid")
    public EvalReaderDTO readEval(Long postId) {
        EvalReaderDTO evalReaderDTO = evalService.reader(postId);

        log.info(evalReaderDTO);

        return evalReaderDTO;
    }

    // 평가 게시글 아이디로 게시글 정보 가져오기
    @GetMapping("/readAnyEval")
    public ResponseEntity<ResultResponse> readAnyEval() {
        List<EvalReaderDTO> evalReaderDTOS = evalService.readAll();

        log.info(evalReaderDTOS.size());
        if(evalReaderDTOS.size() == 0)
        {
            return ResponseEntity.ok(ResultResponse.of(FIND_POST_FAIL));
        }

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS,evalReaderDTOS));
    }

    @GetMapping("/readNextEval")
    public ResponseEntity<ResultResponse> getNextEvaluation()
    {
        final EvalReaderDTO evalPage = evalService.getEvalPage(0,1);

        if(evalPage == null)
        {
            return ResponseEntity.ok(ResultResponse.of(FIND_POST_FAIL));
        }

        return ResponseEntity.ok(ResultResponse.of(FIND_RECENT10POSTS_SUCCESS,evalPage));
    }

    @GetMapping("/readbyuid")
    public ResponseEntity<ResultResponse> readpostbyUID(String userID) {
        List<EvalReaderDTO> evalReaderDTOS = evalService.readerUID(userID);

        log.info(evalReaderDTOS);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS,evalReaderDTOS));
    }
    
}
