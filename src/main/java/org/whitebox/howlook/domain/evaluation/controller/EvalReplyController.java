package org.whitebox.howlook.domain.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.whitebox.howlook.domain.evaluation.dto.EvalRegisterDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalReplyDTO;
import org.whitebox.howlook.domain.evaluation.repository.EvalReplyRepository;
import org.whitebox.howlook.domain.evaluation.service.EvalReplyService;
import org.whitebox.howlook.domain.evaluation.service.EvalReplyServiceImpl;
import org.whitebox.howlook.domain.evaluation.service.EvalService;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

import static org.whitebox.howlook.global.result.ResultCode.CREATE_POST_FAIL;
import static org.whitebox.howlook.global.result.ResultCode.REGISTER_SUCCESS;

@RestController
@RequestMapping("/eval/reply")
@Log4j2
@RequiredArgsConstructor
public class EvalReplyController {
    private final EvalReplyService evalReplyService;

    @PostMapping(value = "/register")
    public ResponseEntity<ResultResponse> registerEval(@Valid @ModelAttribute EvalReplyDTO evalReplyDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes)
    {
        evalReplyService.register(evalReplyDTO);
        return ResponseEntity.ok(ResultResponse.of(REGISTER_SUCCESS, evalReplyDTO));
    }
}
