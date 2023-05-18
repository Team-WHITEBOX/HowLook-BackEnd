package org.whitebox.howlook.domain.report.service;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ReportPostService {
    public ResponseEntity<String> reportPost(Long postId);
    //public ResponseEntity<String> reportAll(Long postId) throws IOException;
    void reportAll(Long postId) throws  IOException;
}
