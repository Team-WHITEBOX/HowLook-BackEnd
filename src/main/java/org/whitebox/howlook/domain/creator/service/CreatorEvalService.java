package org.whitebox.howlook.domain.creator.service;

import org.whitebox.howlook.domain.creator.dto.CreatorEvalRegisterDTO;

public interface CreatorEvalService {
    // 평가글 적기
    long registerCreatorEval(CreatorEvalRegisterDTO creatorEvalRegisterDTO);
}