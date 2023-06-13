package org.whitebox.howlook.domain.evaluation.service;

import org.whitebox.howlook.domain.evaluation.dto.CreatorDataDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorReplyDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorReviewDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalDataDTO;

import java.util.List;

public interface CreatorReplyService {

    public void register(CreatorReplyDTO creatorReplyDTO);

    public List<CreatorReviewDTO> ReadDataByPostId(Long postId);
}
