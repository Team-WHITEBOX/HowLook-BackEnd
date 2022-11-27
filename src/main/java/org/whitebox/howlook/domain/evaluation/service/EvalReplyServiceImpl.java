package org.whitebox.howlook.domain.evaluation.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

        // 이미 달은 평가라면 달리지않게 find 후 조건문 생성
        EvalReply temp = evalReplyRepository.findByPostid(pid);
        if(temp == null) {

            Evaluation evaluation = evalRepository.findByPid(pid);
            evalReply.setEvaluation(evaluation);


            evalReplyRepository.save(evalReply);
        }
    }

}
