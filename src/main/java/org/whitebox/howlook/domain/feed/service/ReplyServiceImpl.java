package org.whitebox.howlook.domain.feed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.dto.ReplyDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.Reply;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;
import org.whitebox.howlook.domain.feed.repository.ReplyRepository;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.global.util.AccountUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;
    private final FeedRepository feedRepository;
    private final AccountUtil accountUtil;
    private final ModelMapper modelMapper;
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
    public void All_read() {
        
    }
}