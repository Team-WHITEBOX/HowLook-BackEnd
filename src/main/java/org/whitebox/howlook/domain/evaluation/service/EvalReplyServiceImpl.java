package org.whitebox.howlook.domain.evaluation.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.whitebox.howlook.domain.evaluation.dto.EvalDataDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalRegisterDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalReplyDTO;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.evaluation.repository.EvalReplyRepository;
import org.whitebox.howlook.domain.evaluation.repository.EvalRepository;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;
import org.whitebox.howlook.global.util.AccountUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        // 현재 내가 쓰려고하는 포스트 아이디
        Long pid = evalReplyDTO.getPid();

        if(pid == null)
            return;

        // 이미 달은 평가라면 달리지않게 find 후 조건문 생성
        EvalReply temp = evalReplyRepository.findMyReplyByPostid(pid,accountUtil.getLoginMember().getMid());
        //evalReaderDTO.setUserPostInfo(new UserPostInfoResponse(evalList.get(i).getMember()));

        if(temp == null) {

            Evaluation evaluation = evalRepository.findByPid(pid);
            evalReply.setEvaluation(evaluation);


            evalReplyRepository.save(evalReply);
        }
    }

    @Override
    public EvalDataDTO ReadDateByPostId(Long NPostId)
    {
        float MAX = 0;
        float MIN = 10;
        float averScore = 0;
        float mScore = 0;
        float fScore = 0;
        Long mCount = 0L;
        Long fCount = 0L;
        Long rCount = 0L;

        List<EvalReply> evalReplies = evalReplyRepository.findBypid(NPostId);

        for(EvalReply reply : evalReplies)
        {
            if(reply.getMember().getGender() == 'm') // 남자가 쓴 평가라면
            {
                mScore += reply.getScore();
                mCount += 1;
            }
            else if(reply.getMember().getGender() == 'f') // 여자가 쓴 평가라면
            {
                fScore += reply.getScore();
                fCount += 1;
            }

            averScore += reply.getScore();
            rCount += 1;

            if(reply.getScore() > MAX)
                MAX = reply.getScore();

            if(reply.getScore() < MIN)
                MIN = reply.getScore();
        }

        if(averScore != 0 && rCount != 0)
            averScore = averScore/rCount;

        if(mScore != 0 && mCount != 0)
            mScore = mScore/mCount;

        if(fScore != 0 && fCount != 0)
            fScore = fScore/fCount;

        EvalDataDTO evalDataDTO = new EvalDataDTO();
        evalDataDTO.setAverageScore(averScore);
        evalDataDTO.setMaleScore(mScore);
        evalDataDTO.setFemaleScore(fScore);
        evalDataDTO.setReplyCount(rCount);
        evalDataDTO.setMaleCount(mCount);
        evalDataDTO.setFemaleCount(fCount);
        evalDataDTO.setMaxScore(MAX);
        evalDataDTO.setMinScore(MIN);

        return evalDataDTO;
    }

}
