package org.whitebox.howlook.domain.evaluation.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.evaluation.dto.CreatorDataDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorReplyDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorReviewDTO;
import org.whitebox.howlook.domain.evaluation.entity.CreatorEval;
import org.whitebox.howlook.domain.evaluation.entity.CreatorReply;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.evaluation.repository.CreatorEvalRepository;
import org.whitebox.howlook.domain.evaluation.repository.CreatorReplyRepository;
import org.whitebox.howlook.domain.evaluation.repository.EvalReplyRepository;
import org.whitebox.howlook.domain.evaluation.repository.EvalRepository;
import org.whitebox.howlook.global.error.exception.BusinessException;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.whitebox.howlook.global.error.ErrorCode.*;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
@Data
public class CreatorReplyServiceImpl implements  CreatorReplyService{

    private final ModelMapper modelMapper;
    final CreatorReplyRepository creatorReplyRepository;
    final CreatorEvalRepository creatorEvalRepository;
    final AccountUtil accountUtil;

    @Override
    public void register(CreatorReplyDTO creatorReplyDTO)
    {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CreatorReply evalReply = modelMapper.map(creatorReplyDTO, CreatorReply.class);
        evalReply.setMember(accountUtil.getLoginMember());

        if(creatorReplyDTO.getScore() > 10) {
            throw new BusinessException(EVAL_REGISTER_FAIL);
        }

        // 현재 내가 쓰려고하는 포스트 아이디
        Long pid = creatorEvalRepository.findByPid(creatorReplyDTO.getPostId()).orElseThrow(
                () -> new EntityNotFoundException(EVAL_NOT_EXIST)
        ).getEvalId();

        // 이미 달은 평가라면 달리지않게 find 후 조건문 생성
        CreatorReply temp = creatorReplyRepository.findMyReplyByPostid(pid,accountUtil.getLoginMember().getMemberId());
        //evalReaderDTO.setUserPostInfo(new UserPostInfoResponse(evalList.get(i).getMember()));

        if(temp == null) {
            CreatorEval evaluation = creatorEvalRepository.findByPid(pid).get();
            evalReply.setEvaluation(evaluation);
            creatorReplyRepository.save(evalReply);
        }
        else
        {
            throw new BusinessException(EVAL_ALREADY_EXSIST);
        }

        return;
    }


    public List<CreatorReviewDTO> ReadDataByPostId(Long postId)
    {
        List<CreatorReply> evalReplies = creatorReplyRepository.findBypid(postId).orElseThrow(() -> new EntityNotFoundException(EVAL_NOT_EXIST));
        List<CreatorReviewDTO> list = new ArrayList<>();

        for(CreatorReply c : evalReplies)
        {
            CreatorReviewDTO creatorReviewDTO = new CreatorReviewDTO();
            creatorReviewDTO.setPostId(postId);
            creatorReviewDTO.setReview(c.getReview());
            creatorReviewDTO.setScore(c.getScore());
            creatorReviewDTO.setNickname(c.getMember().getNickName());
            creatorReviewDTO.setMainPhotoPath(c.getEvaluation().getMainPhotoPath());

            list.add(creatorReviewDTO);

        }

        return list;
    }
}
