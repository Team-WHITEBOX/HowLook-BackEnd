package org.whitebox.howlook.domain.evaluation.service;

import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalRegisterDTO;

public interface EvalService {
    void register(EvalRegisterDTO evalRegisterDTO);
    EvalReaderDTO reader(Long NPostId);
}
