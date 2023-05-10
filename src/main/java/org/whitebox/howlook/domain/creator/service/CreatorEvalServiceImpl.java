package org.whitebox.howlook.domain.creator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.creator.dto.CreatorEvalRegistorDTO;
import org.whitebox.howlook.domain.creator.entity.CreatorEval;
import org.whitebox.howlook.domain.creator.repository.CreatorEvalRepository;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.evaluation.repository.EvalRepository;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;

@Service
@RequiredArgsConstructor
@Log4j2
public class CreatorEvalServiceImpl implements CreatorEvalService{
    private final ModelMapper modelMapper;
    private final AccountUtil accountUtil;
    private final CreatorEvalRepository creatorEvalRepository;
    private final EvalRepository evalRepository;
    @Override
    public long registerCreatorEval(CreatorEvalRegistorDTO creatorEvalRegistorDTO) { // 크리에이터 평가글 등록하기
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CreatorEval creatorEval = modelMapper.map(creatorEvalRegistorDTO,CreatorEval.class);
        Member member = accountUtil.getLoginMember();

        Evaluation evaluation = evalRepository.findById(creatorEvalRegistorDTO.getEvaluation().getPostId()).orElseThrow(()
        -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));

        creatorEval.setMember(member);
        creatorEval.setEvaluation(evaluation);

        long EvalId = creatorEvalRepository.save(creatorEval).getEvalId(); // 레퍼지토리에 저장하면서 EvalId를 저장.
        return EvalId;
    }
}