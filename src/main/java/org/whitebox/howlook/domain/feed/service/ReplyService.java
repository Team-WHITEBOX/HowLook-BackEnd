package org.whitebox.howlook.domain.feed.service;

import org.whitebox.howlook.domain.feed.dto.ReplyDTO;
import org.whitebox.howlook.domain.feed.dto.ReplyReadDTO;
import org.whitebox.howlook.domain.feed.dto.ReplyRegisterDTO;
import org.whitebox.howlook.domain.feed.entity.Reply;

import java.util.List;

public interface ReplyService {
    // 댓글등록 + 대댓글등록 추가구현
    long register_reply(ReplyRegisterDTO replyRegisterDTO);

    // 특정 댓글 조회
    ReplyReadDTO read(Long ReplyId);

    // 특정 댓글 삭제
    void remove(Long ReplyId);
    
    // 특정 댓글 수정
    void modify(ReplyDTO replyDTO);

    List<Reply> getListOfFeed(Long NpostId); // 게시글에 해당하는 댓글들 읽어오기

    void likeReply(Long ReplyId); // 댓글 좋아요

    void unlikeReply(Long ReplyId); // 댓글 좋아요 취소
}
