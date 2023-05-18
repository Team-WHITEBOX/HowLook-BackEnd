package org.whitebox.howlook.domain.evaluation.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.evaluation.dto.EvalDataDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalReplyDTO;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.evaluation.repository.EvalReplyRepository;
import org.whitebox.howlook.domain.evaluation.repository.EvalRepository;
import org.whitebox.howlook.global.error.exception.BusinessException;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.whitebox.howlook.global.error.ErrorCode.*;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
@Data
public class EvalReplyServiceImpl implements EvalReplyService{
    private final ModelMapper modelMapper;
    final EvalReplyRepository evalReplyRepository;
    final EvalRepository evalRepository;
    final AccountUtil accountUtil;

    @Override
    public void register(EvalReplyDTO evalReplyDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        EvalReply evalReply = modelMapper.map(evalReplyDTO, EvalReply.class);
        evalReply.setMember(accountUtil.getLoginMember());

        if(evalReplyDTO.getScore() > 10) {
            throw new BusinessException(EVAL_REGISTER_FAIL);
        }

        // 현재 내가 쓰려고하는 포스트 아이디
        Long pid = evalRepository.findByPid(evalReplyDTO.getPostId()).orElseThrow(
            () -> new EntityNotFoundException(EVAL_NOT_EXIST)
        ).getPostId();

        // 이미 달은 평가라면 달리지않게 find 후 조건문 생성
        EvalReply temp = evalReplyRepository.findMyReplyByPostid(pid,accountUtil.getLoginMember().getMemberId());
        //evalReaderDTO.setUserPostInfo(new UserPostInfoResponse(evalList.get(i).getMember()));

        if(temp == null) {
            Evaluation evaluation = evalRepository.findByPid(pid).get();
            evalReply.setEvaluation(evaluation);
            evalReplyRepository.save(evalReply);
        }
        else
        {
            throw new BusinessException(EVAL_ALREADY_EXSIST);
        }

        return;
    }

    @Override
    public EvalDataDTO ReadDateByPostId(Long postId)
    {
        float maxScore = 0;
        float minScore = 10;
        float averageScore = 0;
        float maleScore = 0;
        float femaleScore = 0;
        Long maleCount = 0L;
        Long femaleCount = 0L;
        Long replyCount = 0L;

        float[] maleScores = new float[5];
        float[] femaleScores = new float[5];
        Long[] maleCounts = new Long[5];
        Long[] femaleCounts = new Long[5];

        Arrays.fill(maleScores,0);
        Arrays.fill(femaleScores,0);
        Arrays.fill(maleCounts,0L);
        Arrays.fill(femaleCounts,0L);

        List<EvalReply> evalReplies = evalReplyRepository.findBypid(postId);

        for(EvalReply reply : evalReplies)
        {
            if(reply.getMember().getGender() == 'M') // 남자가 쓴 평가라면
            {
                maleScore += reply.getScore();
                maleCount += 1;

                int index = (int)((reply.getScore() - 1) / 2);
                maleScores[index] += reply.getScore();
                maleCounts[index] += 1;
            }
            else if(reply.getMember().getGender() == 'F') // 여자가 쓴 평가라면
            {
                femaleScore += reply.getScore();
                femaleCount += 1;

                int index = (int)((reply.getScore() - 1) / 2);
                femaleScores[index] += reply.getScore();
                femaleCounts[index] += 1;
            }

            averageScore += reply.getScore();
            replyCount += 1;

            if(reply.getScore() > maxScore)
                maxScore = reply.getScore();

            if(reply.getScore() < minScore)
                minScore = reply.getScore();
        }

        if(averageScore != 0 && replyCount != 0)
            averageScore = averageScore/replyCount;

        if(maleScore != 0 && maleCount != 0)
            maleScore = maleScore/maleCount;

        if(femaleScore != 0 && femaleCount != 0)
            femaleScore = femaleScore/femaleCount;

        for(int i = 0; i < 5; i++)
        {
            if(maleScores[i] != 0 && maleCounts[i] != 0)
                maleScores[i] = maleScores[i]/maleCounts[i];

            if(femaleScores[i] != 0 && femaleCounts[i] != 0)
                femaleScores[i] = femaleScores[i]/femaleCounts[i];
        }

        EvalDataDTO evalDataDTO = new EvalDataDTO();

        evalDataDTO.setAverageScore(averageScore);
        evalDataDTO.setMaleScore(maleScore);
        evalDataDTO.setFemaleScore(femaleScore);
        evalDataDTO.setReplyCount(replyCount);
        evalDataDTO.setMaleCount(maleCount);
        evalDataDTO.setFemaleCount(femaleCount);
        evalDataDTO.setMaxScore(maxScore);
        evalDataDTO.setMinScore(minScore);
        evalDataDTO.setMaleScores(maleScores);
        evalDataDTO.setMaleCounts(maleCounts);
        evalDataDTO.setFemaleScores(femaleScores);
        evalDataDTO.setFemaleCounts(femaleCounts);

        return evalDataDTO;
    }

}