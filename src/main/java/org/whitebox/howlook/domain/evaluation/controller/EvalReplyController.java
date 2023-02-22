package org.whitebox.howlook.domain.evaluation.controller;

import io.swagger.annotations.ApiOperation;
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
import org.whitebox.howlook.domain.evaluation.dto.EvalReplyDTO;
import org.whitebox.howlook.domain.evaluation.service.EvalReplyService;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

import static org.whitebox.howlook.global.result.ResultCode.*;

@RestController
@RequestMapping("/eval/reply")
@Log4j2
@RequiredArgsConstructor
public class EvalReplyController {
    private final EvalReplyService evalReplyService;


    @ApiOperation(value = "평가 글에 평가 남기기 : 평가 글 아이디, 점수")
    @PostMapping(value = "/register")
    public ResponseEntity<ResultResponse> registerEval(@Valid @ModelAttribute EvalReplyDTO evalReplyDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes)
    {
        evalReplyService.register(evalReplyDTO);

        return ResponseEntity.ok(ResultResponse.of(EVAL_DATA_REGISTER_SUCCESS, evalReplyDTO));
    }
}
