package org.whitebox.howlook.domain.feed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.dto.ReplyDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.Reply;
import org.whitebox.howlook.domain.feed.entity.ReplyLike;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;
import org.whitebox.howlook.domain.feed.repository.ReplyLikeRepository;
import org.whitebox.howlook.domain.feed.repository.ReplyRepository;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.global.util.AccountUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public long register(ReplyDTO replyDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Reply reply =  modelMapper.map(replyDTO, Reply.class);
        Member member = accountUtil.getLoginMember();
        log.info(replyDTO);
        Feed feed = feedRepository.findById(replyDTO.getNPostId()).orElseThrow();
        log.info(feed);
        reply.setMember(member);
        reply.setFeed(feed);
        reply.setLikeCount(0L);
        long CommentId = replyRepository.save(reply).getCommentId();
        return CommentId;
    }

    @Override
    public ReplyDTO read(Long commentId) {
        Optional<Reply> replyOptional = replyRepository.findById(commentId);

        Reply reply = replyOptional.orElseThrow();

        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void remove(Long CommentId) {
        replyRepository.deleteById(CommentId);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {

        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getCommentId());

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
    public void likeReply(Long commentId) { // 댓글 좋아요
        Optional<Reply> replyOptional = replyRepository.findById(commentId);

        Reply reply = replyOptional.orElseThrow();

        reply.Up_LikeCount();

        Member member = accountUtil.getLoginMember();

        replyLikeRepository.save(new ReplyLike(member,reply));
    }

    @Override
    public void unlikeReply(Long commentId) {
        Optional<Reply> replyOptional = replyRepository.findById(commentId);

        Reply reply = replyOptional.orElseThrow();

        reply.Down_LikeCount();

        Member member = accountUtil.getLoginMember();

        Optional<ReplyLike> ReplyLikeOptional = replyLikeRepository.findByMemberAndReply(member,reply);

        ReplyLike replyLike = ReplyLikeOptional.orElseThrow();

        replyLikeRepository.delete(replyLike);
    }
}