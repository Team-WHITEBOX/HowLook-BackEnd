package org.whitebox.howlook.domain.evaluation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalModifyDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalReadDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalRegisterDTO;
import org.whitebox.howlook.domain.evaluation.entity.CreatorEval;
import org.whitebox.howlook.domain.evaluation.repository.CreatorEvalRepository;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.evaluation.repository.EvalRepository;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CreatorEvalServiceImpl implements CreatorEvalService{
    private final ModelMapper modelMapper;
    private final AccountUtil accountUtil;
    private final CreatorEvalRepository creatorEvalRepository;
    private final EvalRepository evalRepository;
    @Override
    public long registerCreatorEval(CreatorEvalRegisterDTO creatorEvalRegisterDTO) { // 크리에이터 평가글 등록하기
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CreatorEval creatorEval = modelMapper.map(creatorEvalRegisterDTO,CreatorEval.class);
        Member member = accountUtil.getLoginMember();

        Evaluation evaluation = evalRepository.findById(creatorEvalRegisterDTO.getEvalId()).orElseThrow(()
        -> new EntityNotFoundException(ErrorCode.POST_NOT_FOUND));

        creatorEval.setMember(member); // 쓴 멤버 정보 등록
        creatorEval.setEvaluation(evaluation); // 평가글 정보 등록
        creatorEval.setContent(creatorEvalRegisterDTO.getContent()); // 평가글 내용 등록
        creatorEval.setScore(creatorEvalRegisterDTO.getPoint()); // 평가 점수 등록

        long EvalId = creatorEvalRepository.save(creatorEval).getEvalId(); // 레퍼지토리에 저장하면서 EvalId를 저장.
        return EvalId;
    }

    @Override
    public CreatorEvalReadDTO readCreatorEval(Long creatorEvalId) { // 특정 크리에이터 평가글 읽어오기
        Optional<CreatorEval> creatorEvalOptional = creatorEvalRepository.findById(creatorEvalId);
        CreatorEval creatorEval = creatorEvalOptional.orElseThrow(() -> new EntityNotFoundException(ErrorCode.CREATOREVAL_NOT_FOUND));
        CreatorEvalReadDTO dto = new CreatorEvalReadDTO(creatorEval);

        return dto;
    }

    @Transactional
    @Override
    public void remove(Long creatorEvalId) {   // 특정 크리에이터 평가글 지우기
        CreatorEval creatorEval = creatorEvalRepository.findById(creatorEvalId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.CREATOREVAL_NOT_FOUND));
        Member member = accountUtil.getLoginMember();

        if(creatorEval.getMember().getMemberId() != member.getMemberId()) {
            throw new EntityNotFoundException(ErrorCode.CREATOREVAL_CANT_DELETE);
        }

        creatorEvalRepository.delete(creatorEval);
    }

    @Transactional
    @Override
    public void modify(CreatorEvalModifyDTO creatorEvalModifyDTO) { // 크리에이터 평가글 수정
        Optional<CreatorEval> CreatorEvalOptional = creatorEvalRepository.findById(creatorEvalModifyDTO.getCreatorEvalId());
        CreatorEval creatorEval = CreatorEvalOptional.orElseThrow(() -> new EntityNotFoundException(ErrorCode.CREATOREVAL_NOT_FOUND));
        Member member = accountUtil.getLoginMember();

        if(creatorEval.getMember().getMemberId() != member.getMemberId()) {
            throw new EntityNotFoundException(ErrorCode.CREATOREVAL_CANT_MODIFY);
        }

        creatorEval.ChangeContent(creatorEvalModifyDTO.getContent());
        creatorEvalRepository.save(creatorEval);
    }

    @Override // 평가글에 해당하는 크리에이터 평가글 읽어오기.
    public List<CreatorEvalReadDTO> getListOfEval(Long EvalId) {
        List<CreatorEval> creatorEvals = creatorEvalRepository.findByEval(EvalId); // 여기서 list 방식으로 repository에서 받아오기
        List<CreatorEvalReadDTO> result = creatorEvals.stream().map(creatorEval -> new CreatorEvalReadDTO(creatorEval)).collect(Collectors.toList());
        return result;
    }
}