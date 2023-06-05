package org.whitebox.howlook.domain.evaluation.service;

import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalModifyDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalReadDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalRegisterDTO;

import java.util.List;

public interface CreatorEvalService {
    // 평가글 적기
   public long registerCreatorEval(CreatorEvalRegisterDTO creatorEvalRegisterDTO);

    public CreatorEvalReadDTO readCreatorEval(Long creatorEvalId);

    public void remove(Long creatorEvalId);

    public void modify(CreatorEvalModifyDTO creatorEvalModifyDTO);

    public List<CreatorEvalReadDTO> getListOfEval(Long EvalId);
}