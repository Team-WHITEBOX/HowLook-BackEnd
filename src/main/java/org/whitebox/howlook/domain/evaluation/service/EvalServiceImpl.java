package org.whitebox.howlook.domain.evaluation.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalRegisterDTO;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.evaluation.dto.repository.EvalRepository;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;
import org.whitebox.howlook.global.util.AccountUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
@Data
public class EvalServiceImpl implements EvalService{
    private final ModelMapper modelMapper;
    final EvalRepository evalRepository;
    @Value("${org.whitebox.upload.path}")
    private String uploadPath; // 저장될 경로

    final AccountUtil accountUtil;
    @Override
    public void register(EvalRegisterDTO evalRegisterDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Evaluation evaluation = modelMapper.map(evalRegisterDTO, Evaluation.class);

        UploadFileDTO uploadFileDTO = evalRegisterDTO.getFiles();
        List<MultipartFile> list = uploadFileDTO.getFiles();
        var multipartFile = list.get(0);

        String originalName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();

        Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);

        try{
            multipartFile.transferTo(savePath);
        }catch (IOException e)
        {
            e.printStackTrace();
        }

        String m_path = uploadPath+"\\"+uuid+"_"+originalName;
        evaluation.setMainPhotoPath(m_path);

        // 작성자 닉네임 컬럼에 값 추가
        evaluation.setWriter(accountUtil.getLoginMemberId());

        evalRepository.save(evaluation);
    }

    @Override
    public EvalReaderDTO reader(Long NPostId)
    {
        Optional<Evaluation> result = evalRepository.findById(NPostId);

        Evaluation eval = result.orElseThrow();
        log.info(eval);

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        EvalReaderDTO evalReaderDTO = modelMapper.map(eval, EvalReaderDTO.class);

        return evalReaderDTO;
    }
}
