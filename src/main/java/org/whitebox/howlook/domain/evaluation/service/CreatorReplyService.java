package org.whitebox.howlook.domain.evaluation.service;

import org.whitebox.howlook.domain.evaluation.dto.CreatorDataDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorReplyDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalDataDTO;

public interface CreatorReplyService {

    public void register(CreatorReplyDTO creatorReplyDTO);

    public CreatorDataDTO ReadDataByPostId(Long postId);
}
