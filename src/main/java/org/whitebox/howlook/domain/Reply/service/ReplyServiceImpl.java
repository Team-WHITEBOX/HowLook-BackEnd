package org.whitebox.howlook.domain.Reply.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.Reply.dto.ReplyDTO;
import org.whitebox.howlook.domain.Reply.entity.reply;
import org.whitebox.howlook.domain.Reply.repository.ReplyRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;


    @Override
    public int register(ReplyDTO replyDTO) { // 댓글 등록
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        reply r = modelMapper.map(replyDTO, reply.class);

        int CommendId = replyRepository.save(r).getCommendId();

        return CommendId;
    }

    @Override
    public ReplyDTO read(int CommentId) {
        Optional<reply> replyOptional = replyRepository.findById(CommentId);
        reply r = replyOptional.orElseThrow();
        return modelMapper.map(r,ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Optional<reply> replyOptional = replyRepository.findById(replyDTO.getCommendId());
        reply r = replyOptional.orElseThrow();
        r.changeContents(replyDTO.getContents()); // 댓글의 내용만 수정 가능
        replyRepository.save(r);
    }

    @Override
    public void remove(int CommentId) {
        replyRepository.deleteById(CommentId);
    }
}
