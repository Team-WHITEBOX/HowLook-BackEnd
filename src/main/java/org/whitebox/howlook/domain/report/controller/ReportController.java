package org.whitebox.howlook.domain.report.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whitebox.howlook.domain.report.dto.ReportDTO;
import org.whitebox.howlook.domain.report.service.ReportPostService;
import org.whitebox.howlook.global.result.ResultCode;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/report")
@Log4j2
@RequiredArgsConstructor
public class ReportController {

    final ReportPostService reportPostService;

    @ApiOperation(value = "신고하려는 postid만 보내주쇼")
    @PostMapping(value="/reportpost")
    public ResponseEntity<ResultCode> reportPosts(ReportDTO reportDTO) {
        reportPostService.report(reportDTO);

        return ResponseEntity.ok(ResultCode.CREATE_POST_SUCCESS);
    }
}
