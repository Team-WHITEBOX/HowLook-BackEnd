package org.whitebox.howlook.domain.evaluation.service;

import org.whitebox.howlook.domain.evaluation.dto.*;

import java.util.List;

public interface CreatorEvalService {
    // 평가글 적기
    public void registerCreatorEval(CreatorEvalRegisterDTO creatorEvalRegisterDTO);

    public CreatorEvalReadDTO readByCreatorEvalId(Long creatorEvalId);

    public List<CreatorEvalReadDTO> getCreatorEvalPage(int page, int size);

    public CreatorEvalPageDTO getCreatorEvalWithHasMore(int page, int size);

    public List<CreatorEvalReadDTO> getListOfUId(String userId);

    public boolean checkEvalHasMyReply(CreatorEvalReadDTO creatorEvalReadDTO);

    public boolean checkMyEvalPost(CreatorEvalReadDTO creatorEvalReadDTO);

    public boolean checkIAMCreator();
}