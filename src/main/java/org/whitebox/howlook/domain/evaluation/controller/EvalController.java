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

        return ResponseEntity.ok(ResultResponse.of(REGISTER_SUCCESS, true));
    }

    // 게시글 아이디로 게시글 정보 가져오기
    @GetMapping("/readbypid")
    public EvalReaderDTO readEval(Long NPostId) {
        EvalReaderDTO evalReaderDTO = evalService.reader(NPostId);

        log.info(evalReaderDTO);

        return evalReaderDTO;
    }

    // 게시글 아이디로 게시글 정보 가져오기
    @GetMapping("/readAnyEval")
    public ResponseEntity<ResultResponse> readAnyEval() {
        List<EvalReaderDTO> evalReaderDTO = evalService.readAll();

        log.info(evalReaderDTO.size());
        if(evalReaderDTO.size() == 0)
        {
            return ResponseEntity.ok(ResultResponse.of(FIND_POST_FAIL));
        }

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS,evalReaderDTO));
    }

    @GetMapping("/readNextEval")
    public ResponseEntity<ResultResponse> getNextEvaluation(@RequestParam int page)
    {
        final List<EvalReaderDTO> evalList = evalService.getEvalPage(page,1);

        if(evalList.size() == 0)
        {
            return ResponseEntity.ok(ResultResponse.of(FIND_POST_FAIL));
        }
        
        return ResponseEntity.ok(ResultResponse.of(FIND_RECENT10POSTS_SUCCESS,evalList));
    }


    @GetMapping("/readbyuid")
    public ResponseEntity<ResultResponse> readFeedbyUID(String UserID) {
        List<EvalReaderDTO> evals = evalService.readerUID(UserID);

        log.info(evals);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS,evals));
    }
    
}
