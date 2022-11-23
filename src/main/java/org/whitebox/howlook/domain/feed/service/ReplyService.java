package org.whitebox.howlook.domain.feed.service;

import org.whitebox.howlook.domain.feed.dto.ReplyDTO;
import org.whitebox.howlook.domain.feed.entity.Reply;

public interface ReplyService {
    // 댓글등록
    long register(ReplyDTO replyDTO);

    // 특정 댓글 조회
    ReplyDTO read(Long CommentId);

    // 전체 댓글 조회
    // void All_read();

    // 특정 댓글 삭제



}
