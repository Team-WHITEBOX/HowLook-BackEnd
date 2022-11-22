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
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

import static org.whitebox.howlook.global.result.ResultCode.CREATE_POST_FAIL;
import static org.whitebox.howlook.global.result.ResultCode.REGISTER_SUCCESS;

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

    //게시물 불러오는 GET으로 매핑한 API구현해야함
    @GetMapping("/read")
    public EvalReaderDTO readEval(Long NPostId) {
        EvalReaderDTO evalReaderDTO = evalService.reader(NPostId);

        log.info(evalReaderDTO);

        return evalReaderDTO;
    }
}
