package org.whitebox.howlook.domain.evaluation.service;

import org.whitebox.howlook.domain.evaluation.dto.EvalDataDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalReplyDTO;

public interface EvalReplyService {
    public void register(EvalReplyDTO evalReplyDTO);

    public EvalDataDTO ReadDateByPostId(Long postId);
}
