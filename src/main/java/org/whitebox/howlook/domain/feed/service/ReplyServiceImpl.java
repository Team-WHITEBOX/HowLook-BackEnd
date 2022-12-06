package org.whitebox.howlook.domain.feed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.dto.ReplyDTO;
import org.whitebox.howlook.domain.feed.dto.ReplyReadDTO;
import org.whitebox.howlook.domain.feed.dto.ReplyRegisterDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.Reply;
import org.whitebox.howlook.domain.feed.entity.ReplyLike;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;
import org.whitebox.howlook.domain.feed.repository.ReplyLikeRepository;
import org.whitebox.howlook.domain.feed.repository.ReplyRepository;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.EntityAlreadyExistException;
import org.whitebox.howlook.global.util.AccountUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;
    private final FeedRepository feedRepository;
    private final AccountUtil accountUtil;
    private final ModelMapper modelMapper;
    private final ReplyLikeRepository replyLikeRepository;
    @Override
    public long register_reply(ReplyRegisterDTO replyRegisterDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Reply reply =  modelMapper.map(replyRegisterDTO, Reply.class);
        Member member = accountUtil.getLoginMember();
        log.info(replyRegisterDTO);
        Feed feed = feedRepository.findById(replyRegisterDTO.getNPostId()).orElseThrow();
        feed.UpCommentCount();
        log.info(feed);
        reply.setMember(member);
        reply.setFeed(feed);
        reply.setParentsId(replyRegisterDTO.getParentId());
        reply.setLikeCount(0L);
        long ReplyId = replyRepository.save(reply).getReplyId();
        feedRepository.save(feed);
        return ReplyId;
    }

    @Override
    public ReplyReadDTO read(Long ReplyId) {
        Optional<Reply> replyOptional = replyRepository.findById(ReplyId);
        Reply reply = replyOptional.orElseThrow();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ReplyReadDTO dto = modelMapper.map(reply, ReplyReadDTO.class);
        dto.setNpostId(reply.getFeed().getNPostId());
        dto.setNickName(reply.getMember().getNickName());
        dto.setProfilePhoto(reply.getMember().getProfilePhoto());
        return dto;
    }

    @Override
    public void remove(Long ReplyId) {
        Reply reply = replyRepository.findById(ReplyId).orElseThrow();
        Feed feed = feedRepository.findById(reply.getFeed().getNPostId()).orElseThrow();
        feed.DownCommentCount();
        feedRepository.save(feed);
        replyRepository.deleteById(ReplyId);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {

        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getReplyId());

        Reply reply = replyOptional.orElseThrow();

        reply.changeText(replyDTO.getContents());

        replyRepository.save(reply);
    }

    @Override // 게시글에 해당하는 댓글 읽어오기.
    public List<Reply> getListOfFeed(Long NpostId) {
        List<Reply> result = replyRepository.listOfFeed(NpostId);
        return result;
    }

    @Override
    public void likeReply(Long ReplyId) { // 댓글 좋아요
        Optional<Reply> replyOptional = replyRepository.findById(ReplyId);

        Reply reply = replyOptional.orElseThrow();

        reply.Up_LikeCount();

        Member member = accountUtil.getLoginMember();

        if(replyLikeRepository.findByMemberAndReply(member,reply).isPresent())
            throw new EntityAlreadyExistException(ErrorCode.COMMENT_LIKE_ALREADY_EXIST);

        replyLikeRepository.save(new ReplyLike(member,reply));
    }

    @Override
    public void unlikeReply(Long ReplyId) {
        Optional<Reply> replyOptional = replyRepository.findById(ReplyId);

        Reply reply = replyOptional.orElseThrow();

        reply.Down_LikeCount();

        Member member = accountUtil.getLoginMember();

        Optional<ReplyLike> ReplyLikeOptional = replyLikeRepository.findByMemberAndReply(member,reply);

        ReplyLike replyLike = ReplyLikeOptional.orElseThrow();

        replyLikeRepository.delete(replyLike);
    }
}