package org.whitebox.howlook.domain.Reply.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.Reply.dto.ReplyDTO;
import org.whitebox.howlook.domain.Reply.entity.CommentLike;
import org.whitebox.howlook.domain.Reply.entity.reply;
import org.whitebox.howlook.domain.member.entity.Member;
//import org.whitebox.howlook.dto.PageRequestDTO;
//import org.whitebox.howlook.dto.PageResponseDTO;

public interface ReplyService {
    int register(ReplyDTO replyDTO);

    ReplyDTO read(int CommendId);

    void modify(ReplyDTO replyDTO);

    void remove(int CommendId);
//    PageResponse<ReplyDTO> getListOfBoard(int bno, PageRequestDTO pageRequestDTO);

    @Transactional
    public void likeComment(int commentId) {
        final reply r = getCommentWithPostAndMember(commentId);
        final Member loginMember = authUtil.getLoginMember();

        if (commentLikeRepository.findByMemberAndComment(loginMember, comment).isPresent())
            throw new EntityAlreadyExistException(COMMENT_LIKE_ALREADY_EXIST);

        commentLikeRepository.save(new CommentLike(loginMember, r));
        alarmService.alert(LIKE_COMMENT, comment.getMember(), comment.getPost(), comment);
    }

    @Transactional
    public void unlikeComment(Long commentId) {
        final reply r = getCommentWithMember(commentId);
        final Member loginMember = authUtil.getLoginMember();
        final CommentLike commentLike = getCommentLike(loginMember,r);
        commentLikeRepository.delete(commentLike);
        alarmService.delete(LIKE_COMMENT, comment.getMember(), comment);
    }
}
