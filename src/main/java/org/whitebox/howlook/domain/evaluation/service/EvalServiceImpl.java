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
import org.whitebox.howlook.domain.evaluation.dto.EvalPageDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalRegisterDTO;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.evaluation.repository.EvalReplyRepository;
import org.whitebox.howlook.domain.evaluation.repository.EvalRepository;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;
import org.whitebox.howlook.global.util.LocalUploader;
import org.whitebox.howlook.global.util.S3Uploader;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.whitebox.howlook.global.error.ErrorCode.EVAL_NOT_EXIST;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
@Data
public class EvalServiceImpl implements EvalService {
    private final ModelMapper modelMapper;
    final EvalRepository evalRepository;
    final EvalReplyRepository evalReplyRepository;
    @Value("${org.whitebox.upload.path}")
    private String uploadPath; // 저장될 경로

    final AccountUtil accountUtil;
    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;

    @Value("${org.whitebox.server.upload}")
    public String isServer;

    @Override
    public void register(EvalRegisterDTO evalRegisterDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Evaluation evaluation = modelMapper.map(evalRegisterDTO, Evaluation.class);

        UploadFileDTO uploadFileDTO = evalRegisterDTO.getFiles();

        List<String> uploadedFilePaths = new ArrayList<>();
        for (MultipartFile file : uploadFileDTO.getFiles()) {
            uploadedFilePaths.addAll(localUploader.uploadLocal(file));
        }

        String m_path;

        if (isServer.equals("true")) {
            List<String> s3Paths =
                    uploadedFilePaths.stream().map(s3Uploader::upload).collect(Collectors.toList());
            m_path = s3Paths.get(0);
        } else {
            m_path = uploadedFilePaths.get(0);
        }


        evaluation.setMainPhotoPath(m_path);
        evaluation.setMember(accountUtil.getLoginMember());

        evalRepository.save(evaluation);
    }

    @Override
    public EvalReaderDTO reader(Long postId) {
        Optional<Evaluation> result = evalRepository.findById(postId);

        Evaluation eval = result.orElseThrow(() -> new EntityNotFoundException(EVAL_NOT_EXIST));
        log.info(eval);

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        EvalReaderDTO evalReaderDTO = modelMapper.map(eval, EvalReaderDTO.class);
        evalReaderDTO.setPostId(eval.getPostId());
        evalReaderDTO.setMainPhotoPath(eval.getMainPhotoPath());

        List<EvalReply> evalReplies = evalReplyRepository.findBypid(evalReaderDTO.getPostId()).orElseThrow(() -> new EntityNotFoundException(EVAL_NOT_EXIST));
        float averScore = 0;
        Long rCount = 0L;
        for (EvalReply r : evalReplies) {
            averScore += r.getScore();
            rCount += 1;
        }

        if (averScore != 0 && rCount != 0)
            averScore = averScore / rCount;

        evalReaderDTO.setAverageScore(averScore);

        return evalReaderDTO;
    }

    @Override
    public List<EvalReaderDTO> readAll() {
        List<Evaluation> evalList = evalRepository.findAll();
        List<EvalReaderDTO> readerDTOList = new ArrayList<>();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        for (int i = 0; i < evalList.size(); i++) {

            EvalReaderDTO evalReaderDTO = modelMapper.map(evalList.get(i), EvalReaderDTO.class);


            // 이미 달은 평가라면 반환하지 않게
            EvalReply temp = evalReplyRepository
                    .findMyReplyByPostid(evalReaderDTO.getPostId(), accountUtil.getLoginMember().getMemberId());

            if (temp == null) {
                readerDTOList.add(evalReaderDTO);
            }

        }
        return readerDTOList;
    }

    @Override
    public List<EvalReaderDTO> readerUID(String UserID) {
        List<Evaluation> evals = evalRepository.findBymemberId(UserID);

        if(evals.size() == 0)
            throw new EntityNotFoundException(EVAL_NOT_EXIST);

        List<EvalReaderDTO> result = new ArrayList<>();
        for (Evaluation eval : evals) {
            EvalReaderDTO evalReaderDTO = new EvalReaderDTO().builder()
                    .postId(eval.getPostId())
                    .mainPhotoPath(eval.getMainPhotoPath()).build();
            result.add(evalReaderDTO);

            List<EvalReply> evalReplies = evalReplyRepository.findBypid(evalReaderDTO.getPostId()).orElseThrow(() -> new EntityNotFoundException(EVAL_NOT_EXIST));
            float averScore = 0;
            Long rCount = 0L;
            for (EvalReply r : evalReplies) {
                averScore += r.getScore();
                rCount += 1;
            }

            if (averScore != 0 && rCount != 0)
                averScore = averScore / rCount;

            evalReaderDTO.setAverageScore(averScore);
        }
        return result;
    }

    @Override
    public List<EvalReaderDTO> getEvalPage(int page, int size) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        final Pageable pageable = PageRequest.of(page, size);
        Page<EvalReaderDTO> evalPage = evalRepository.findEvalReaderDTOPage(pageable);
        List<EvalReaderDTO> evalListFromPage = evalPage.getContent();

        List<EvalReaderDTO> readerDTOList = new ArrayList();

        for (int i = 0; i < evalListFromPage.size(); i++) {

            EvalReaderDTO evalReaderDTO = modelMapper.map(evalListFromPage.get(i), EvalReaderDTO.class);

            if (checkEvalHasMyReply(evalReaderDTO) && !checkMyEvalPost(evalReaderDTO)) {
                readerDTOList.add(evalReaderDTO);
            } else {
                List<EvalReaderDTO> temp = getEvalPage(page + 1, size);
                if (temp != null) {
                    for (int j = 0; j < temp.size(); j++)
                        readerDTOList.add(temp.get(j));
                }
            }
        }
        return readerDTOList;
    }

    @Override
    public EvalPageDTO getEvalWithHasMore(int page, int size) {
        final List<EvalReaderDTO> evalPage = getEvalPage(page, size);

        if (evalPage == null || evalPage.size() == 0) // || evalPage.getPostId() == null)
        {
            return null;
        }

        EvalPageDTO evalPageDTO = new EvalPageDTO(evalPage.get(0));

        if (evalPage.size() >= 2 && evalPage.get(0).getPostId() != evalPage.get(1).getPostId()) {
            // hasMore = 1
            evalPageDTO.setHasMore(1L);
        } else {
            // hasMore = 0
            evalPageDTO.setHasMore(0L);
        }

        return evalPageDTO;
    }

    public boolean checkEvalHasMyReply(EvalReaderDTO evalReaderDTO) {
        // 내가 달은 평가가 없다면 true 리턴
        EvalReply temp = evalReplyRepository
                .findMyReplyByPostid(evalReaderDTO.getPostId(), accountUtil.getLoginMember().getMemberId());

        if (temp == null)
            return true;

        return false;
    }

    public boolean checkMyEvalPost(EvalReaderDTO evalReaderDTO) {
        // 내가 쓴 글이라면 true 리턴
        Evaluation evaluation = evalRepository.findByPid(evalReaderDTO.getPostId()).get();

        if (evaluation.getMember().getMemberId() == accountUtil.getLoginMember().getMemberId())
            return true;

        return false;
    }


    @Override
    public List<EvalReaderDTO> readAllwithoutMine() {
        List<Evaluation> evalList = evalRepository.findAll();
        List<EvalReaderDTO> readerDTOList = new ArrayList<>();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        for (int i = 0; i < evalList.size(); i++) {
            EvalReaderDTO evalReaderDTO = modelMapper.map(evalList.get(i), EvalReaderDTO.class);

            if (checkEvalHasMyReply(evalReaderDTO) && !checkMyEvalPost(evalReaderDTO)) {
                readerDTOList.add(evalReaderDTO);
            }

        }

        return readerDTOList;
    }
}
