package org.whitebox.howlook.domain.report.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whitebox.howlook.domain.report.service.ReportPostService;
import org.whitebox.howlook.global.result.ResultCode;

import java.io.IOException;

@RestController
@RequestMapping("/report")
@Log4j2
@RequiredArgsConstructor
public class ReportController {

    final ReportPostService reportPostService;

    @ApiOperation(value = "신고하려는 postid만 보내주쇼")
    @PostMapping(value="/reportpost")
    public ResponseEntity<ResultCode> reportPosts(Long postId) throws IOException {
        reportPostService.reportPost(postId);

        return ResponseEntity.ok(ResultCode.REPORT_POST_SUCCESS);
    }
}
