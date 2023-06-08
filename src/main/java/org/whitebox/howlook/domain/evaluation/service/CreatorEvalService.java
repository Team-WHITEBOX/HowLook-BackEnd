package org.whitebox.howlook.domain.evaluation.service;

import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalModifyDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalReadDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalRegisterDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;

import java.util.List;

public interface CreatorEvalService {
    // 평가글 적기
   public void registerCreatorEval(CreatorEvalRegisterDTO creatorEvalRegisterDTO);

    public CreatorEvalReadDTO readByCreatorEvalId(Long creatorEvalId);

    public
}