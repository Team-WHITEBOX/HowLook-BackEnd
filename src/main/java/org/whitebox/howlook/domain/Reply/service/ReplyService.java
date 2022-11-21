package org.whitebox.howlook.domain.Reply.service;

import org.whitebox.howlook.domain.Reply.dto.ReplyDTO;
import org.whitebox.howlook.domain.Reply.entity.CommentLike;
//import org.whitebox.howlook.dto.PageRequestDTO;
//import org.whitebox.howlook.dto.PageResponseDTO;

public interface ReplyService {

    int register(ReplyDTO replyDTO);

    ReplyDTO read(int CommendId);

    void modify(ReplyDTO replyDTO);

    void remove(int CommendId);
//    PageResponse<ReplyDTO> getListOfBoard(int bno, PageRequestDTO pageRequestDTO);

    void likeComment(int commentId); // 댓글 좋아요

    void unlikeComment(int commentId); // 댓글 싫어요
}
