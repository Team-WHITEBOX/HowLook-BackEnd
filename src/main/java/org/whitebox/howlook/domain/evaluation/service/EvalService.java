package org.whitebox.howlook.domain.evaluation.service;

import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalRegisterDTO;

import java.util.List;

public interface EvalService {
    void register(EvalRegisterDTO evalRegisterDTO);
    EvalReaderDTO reader(Long NPostId);
    public List<EvalReaderDTO> readerUID(String UserID);
}
