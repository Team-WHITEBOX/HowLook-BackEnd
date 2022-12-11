package org.whitebox.howlook.domain.evaluation.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalRegisterDTO;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.evaluation.repository.EvalReplyRepository;
import org.whitebox.howlook.domain.evaluation.repository.EvalRepository;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;
import org.whitebox.howlook.domain.upload.service.UploadService;
import org.whitebox.howlook.global.util.AccountUtil;
import org.whitebox.howlook.global.util.LocalUploader;
import org.whitebox.howlook.global.util.S3Uploader;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
@Data
public class EvalServiceImpl implements EvalService{
    private final ModelMapper modelMapper;
    final EvalRepository evalRepository;
    final EvalReplyRepository evalReplyRepository;
    @Value("${org.whitebox.upload.path}")
    private String uploadPath; // 저장될 경로

    final AccountUtil accountUtil;
    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;

    @Override
    public void register(EvalRegisterDTO evalRegisterDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Evaluation evaluation = modelMapper.map(evalRegisterDTO, Evaluation.class);

        UploadFileDTO uploadFileDTO = evalRegisterDTO.getFiles();

        List<String> uploadedFilePaths = new ArrayList<>();
        for(MultipartFile file:uploadFileDTO.getFiles()){
            uploadedFilePaths.addAll(localUploader.uploadLocal(file));
        }

        List<String> s3Paths =
                uploadedFilePaths.stream().map(s3Uploader::upload).collect(Collectors.toList());


        String m_path = s3Paths.get(0);
        evaluation.setMainPhotoPath(m_path);
        evaluation.setMember(accountUtil.getLoginMember());

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
        evalReaderDTO.setUserPostInfo(new UserPostInfoResponse(eval.getMember()));

        return evalReaderDTO;
    }

    @Override
    public List<EvalReaderDTO> readAll()
    {
        List<Evaluation> evalList = evalRepository.findAll();
        List<EvalReaderDTO> readerDTOList = new ArrayList<>();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        for(int i = 0; i < evalList.size(); i++) {

            EvalReaderDTO evalReaderDTO = modelMapper.map(evalList.get(i), EvalReaderDTO.class);
            evalReaderDTO.setUserPostInfo(new UserPostInfoResponse(evalList.get(i).getMember()));


            // 이미 달은 평가라면 반환하지 않게
            EvalReply temp = evalReplyRepository
                    .findMyReplyByPostid(evalReaderDTO.getNPostId(),accountUtil.getLoginMember().getMid());

            if(temp == null) {
                readerDTOList.add(evalReaderDTO);
            }

        }
        return readerDTOList;
    }
    @Override
    public List<EvalReaderDTO> readerUID(String UserID) {
        List<Evaluation> evals = evalRepository.findByMid(UserID);
        List<EvalReaderDTO> result = new ArrayList<>();
        for(Evaluation eval : evals){
            EvalReaderDTO evalReaderDTO = new EvalReaderDTO().builder()
                    .NPostId(eval.getNPostId()).userPostInfo(new UserPostInfoResponse(eval.getMember()))
                    .modDate(eval.getModDate()).regDate(eval.getRegDate()).MainPhotoPath(eval.getMainPhotoPath()).build();
            result.add(evalReaderDTO);
        }

        return result;
    }

    @Override
    public Page<EvalReaderDTO> getEvalPage(int page,int size)
    {
        final Pageable pageable = PageRequest.of(page,size);

        Page<EvalReaderDTO> evalPage = evalRepository.findEvalReaderDTOPage(pageable);
        List<EvalReaderDTO> evalList = evalPage.getContent();

        List<EvalReaderDTO> readerDTOList = new ArrayList<>();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        for(int i = 0; i < evalList.size(); i++) {

            EvalReaderDTO evalReaderDTO = modelMapper.map(evalList.get(i), EvalReaderDTO.class);
            evalReaderDTO.setUserPostInfo(evalList.get(i).getUserPostInfo());


            // 이미 달은 평가라면 반환하지 않게
            EvalReply temp = evalReplyRepository
                    .findMyReplyByPostid(evalReaderDTO.getNPostId(),accountUtil.getLoginMember().getMid());

            if(temp == null) {
                readerDTOList.add(evalReaderDTO);
            }

        }
        return evalPage;
    }
}
