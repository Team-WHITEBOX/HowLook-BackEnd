package org.whitebox.howlook.domain.post.service;

import org.springframework.data.domain.Page;
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

    // 게시글에 해당하는 댓글들 읽어오기
    List<ReplyReadDTO> getListOfPost(Long postId);

    // 게시글에 해당하는 댓글들 페이지 형식으로 읽어오기
    Page<ReplyReadDTO> getReplyPage(Long postId, int page, int size);

    void likeReply(Long replyId); // 댓글 좋아요

    void unlikeReply(Long replyId); // 댓글 좋아요 취소
}