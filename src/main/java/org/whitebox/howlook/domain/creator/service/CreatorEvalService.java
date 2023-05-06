package org.whitebox.howlook.domain.creator.service;

import org.whitebox.howlook.domain.creator.dto.CreatorEvalRegistorDTO;

public interface CreatorEvalService {
    // 평가글 적기
    long registerCreatorEval(CreatorEvalRegistorDTO creatorEvalRegistorDTO);
}
