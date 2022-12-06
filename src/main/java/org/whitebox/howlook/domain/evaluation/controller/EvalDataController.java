package org.whitebox.howlook.domain.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whitebox.howlook.domain.evaluation.dto.EvalDataDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalReplyDTO;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.service.EvalReplyService;
import org.whitebox.howlook.domain.evaluation.service.EvalService;

import java.util.List;

@RestController
@RequestMapping("/eval")
@Log4j2
@RequiredArgsConstructor
public class EvalDataController {
    private final EvalReplyService evalReplyService;

    // 게시글 아이디로 게시글 정보 가져오기
    @GetMapping("/getReplyData")
    public EvalDataDTO getReplyData(Long NPostId) {

        EvalDataDTO evalDataDTO = evalReplyService.ReadDateByPostId(NPostId);

        return evalDataDTO;
    }

}