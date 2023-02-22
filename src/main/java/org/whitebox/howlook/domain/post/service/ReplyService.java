package org.whitebox.howlook.domain.post.service;

import org.whitebox.howlook.domain.post.dto.ReplyModifyDTO;
import org.whitebox.howlook.domain.post.dto.ReplyReadDTO;
import org.whitebox.howlook.domain.post.dto.ReplyRegisterDTO;

import java.util.List;

public interface ReplyService {
    // 댓글등록 + 대댓글등록 추가구현
    long register_reply(ReplyRegisterDTO replyRegisterDto);

    // 특정 댓글 조회
    ReplyReadDTO read(Long replyId);

    // 특정 댓글 삭제
    void remove(Long ReplyId);
    
    // 특정 댓글 수정
    void modify(ReplyModifyDTO replyModifyDTO, Long replyId);

    List<ReplyReadDTO> getListOfpost(Long postId); // 게시글에 해당하는 댓글들 읽어오기

    void likeReply(Long replyId); // 댓글 좋아요

    void unlikeReply(Long replyId); // 댓글 좋아요 취소
}