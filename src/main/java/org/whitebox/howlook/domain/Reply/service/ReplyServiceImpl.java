package org.whitebox.howlook.domain.Reply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.Reply.dto.ReplyDTO;
import org.whitebox.howlook.domain.Reply.entity.CommentLike;
import org.whitebox.howlook.domain.Reply.entity.Reply;
import org.whitebox.howlook.domain.Reply.repository.ReplyRepository;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.global.util.AccountUtil;

import java.util.Optional;

import static org.whitebox.howlook.global.error.ErrorCode.COMMENT_LIKE_ALREADY_EXIST;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    private final AccountUtil accountUtil;

    @Override
    public int register(ReplyDTO replyDTO) { // 댓글 등록
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Reply r = modelMapper.map(replyDTO, Reply.class);

        int CommendId = replyRepository.save(r).getCommendId();

        return CommendId;
    }

    @Override
    public ReplyDTO read(int CommentId) {
        Optional<Reply> replyOptional = replyRepository.findById(CommentId);
        Reply r = replyOptional.orElseThrow();
        return modelMapper.map(r,ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getCommendId());
        Reply r = replyOptional.orElseThrow();
        r.changeContents(replyDTO.getContents()); // 댓글의 내용만 수정 가능
        replyRepository.save(r);
    }

    @Override
    public void remove(int CommentId) {
        replyRepository.deleteById(CommentId);
    }

    @Transactional
    public void likeComment(int commentId) {
        final Reply r = getCommentWithPostAndMember(commentId);
        final Member loginMember = accountUtil.getLoginMember();

        if (commentLikeRepository.findByMemberAndComment(loginMember, r).isPresent())
            throw new EntityAlreadyExistException(COMMENT_LIKE_ALREADY_EXIST);

        commentLikeRepository.save(new CommentLike(loginMember, r));
//      alarmService.alert(LIKE_COMMENT, comment.getMember(), comment.getPost(), comment);
    }

    @Transactional
    public void unlikeComment(Long commentId) {
        final Reply r = getCommentWithMember(commentId);
        final Member loginMember = AccountUtil.getLoginMember();
        final CommentLike commentLike = getCommentLike(loginMember,r);
        commentLikeRepository.delete(commentLike);
        alarmService.delete(LIKE_COMMENT, comment.getMember(), comment);
    }

    private Reply getCommentWithPostAndMember(int commentId) {
        return replyRepository.findWithPostAndMem
    }
}
