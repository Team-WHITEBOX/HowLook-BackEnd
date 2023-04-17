package org.whitebox.howlook.domain.report.service;

import org.springframework.http.ResponseEntity;
import org.whitebox.howlook.domain.report.dto.ReportDTO;

public interface ReportPostService {
    public ResponseEntity<String> reportPost(Long postId);
}
