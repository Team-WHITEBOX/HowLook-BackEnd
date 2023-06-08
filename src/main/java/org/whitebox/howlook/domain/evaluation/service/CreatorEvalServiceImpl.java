package org.whitebox.howlook.domain.evaluation.service;

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
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalReadDTO;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalRegisterDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.entity.CreatorEval;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.evaluation.repository.CreatorEvalRepository;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.payment.entity.UserCash;
import org.whitebox.howlook.domain.payment.exception.LackOfLubyException;
import org.whitebox.howlook.domain.payment.repository.UserCashRepository;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;
import org.whitebox.howlook.global.util.AccountUtil;
import org.whitebox.howlook.global.util.LocalUploader;
import org.whitebox.howlook.global.util.S3Uploader;

import java.util.ArrayList;
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

    @Value("${org.whitebox.upload.path}")
    private String uploadPath; // 저장될 경로
    @Value("${org.whitebox.server.upload}")
    public String isServer;
    private final LocalUploader localUploader;
    private final S3Uploader s3Uploader;
    private final UserCashRepository userCashRepository;
    @Override
    public void registerCreatorEval(CreatorEvalRegisterDTO creatorEvalRegisterDTO) { // 크리에이터 평가 등록
        Member member = accountUtil.getLoginMember();
        UserCash userCash = userCashRepository.findByMember(member);

        if(userCash.getRuby() > 20) {
            userCash.payRuby(20);
            userCashRepository.save(userCash);

            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            CreatorEval creatorEval = modelMapper.map(creatorEvalRegisterDTO, CreatorEval.class);

            // 여기까지 프로필 올리는 코드
            UploadFileDTO uploadFileDTO = creatorEvalRegisterDTO.getFiles();

            List<String> uploadedFilePaths = new ArrayList<>();
            for(MultipartFile file:uploadFileDTO.getFiles()){
                uploadedFilePaths.addAll(localUploader.uploadLocal(file));
            }

            String m_path;

            if(isServer.equals("true")) {
                List<String> s3Paths = uploadedFilePaths.stream().map(s3Uploader::upload).collect(Collectors.toList());
                m_path = s3Paths.get(0);
            }

            else
            {
                m_path = uploadedFilePaths.get(0);
            }
            //

            creatorEval.setMainPhotoPath(m_path);
            creatorEval.setMember(member); // 쓴 멤버 정보 등록


            creatorEvalRepository.save(creatorEval);
        }

        else {
            throw new LackOfLubyException();
        }
    }

    @Override
    public CreatorEvalReadDTO readByCreatorEvalId(Long creatorEvalId)
    {
        Optional<CreatorEval> result = creatorEvalRepository.findById(creatorEvalId);

        CreatorEval creatorEval = result.orElseThrow();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CreatorEvalReadDTO creatorEvalReadDTO = modelMapper.map(creatorEval, CreatorEvalReadDTO.class);

        creatorEvalReadDTO.setAverageScore(0); // 크리에이터 평가글 댓글 생성전까지 임시 값.

        // 크리에이터 평가글 댓글들에 대한 점수 구하기
//        List<EvalReply> evalReplies = evalReplyRepository.findBypid(evalReaderDTO.getPostId());
//
//        float averScore = 0;
//        Long rCount = 0L;
//
//        for(EvalReply r : evalReplies)
//        {
//            averScore += r.getScore();
//            rCount += 1;
//        }
//
//        if(averScore != 0 && rCount != 0)
//            averScore = averScore/rCount;
//
//        evalReaderDTO.setAverageScore(averScore);

        return creatorEvalReadDTO;
    }

    @Override
    public List<CreatorEvalReadDTO> getCreatorEvalPage(int page, int size)
    {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        final Pageable pageable = PageRequest.of(page,size);
        Page<EvalReaderDTO> evalPage = evalRepository.findEvalReaderDTOPage(pageable);
        List<EvalReaderDTO> evalListFromPage = evalPage.getContent();

        List<EvalReaderDTO> readerDTOList = new ArrayList();

        for(int i = 0; i < evalListFromPage.size(); i++) {

            EvalReaderDTO evalReaderDTO = modelMapper.map(evalListFromPage.get(i), EvalReaderDTO.class);

            if(checkEvalHasMyReply(evalReaderDTO) && !checkMyEvalPost(evalReaderDTO) ) {
                readerDTOList.add(evalReaderDTO);
            }
            else{
                List<EvalReaderDTO> temp = getEvalPage(page+1,size);
                if(temp != null) {
                    for (int j = 0; j < temp.size(); j++)
                        readerDTOList.add(temp.get(j));
                }
            }
        }
        return readerDTOList;
    }
}