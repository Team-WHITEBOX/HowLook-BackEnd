package org.whitebox.howlook.domain.evaluation.service;

import org.springframework.data.domain.Page;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalRegisterDTO;

import java.util.List;

public interface EvalService {
    void register(EvalRegisterDTO evalRegisterDTO);
    EvalReaderDTO reader(Long postId);
    public List<EvalReaderDTO> readerUID(String UserID);
    public List<EvalReaderDTO> readAll();
    public EvalReaderDTO getEvalPage(int page, int size);
}
