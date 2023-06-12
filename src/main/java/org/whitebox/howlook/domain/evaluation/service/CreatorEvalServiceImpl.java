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
import org.whitebox.howlook.domain.evaluation.dto.*;
import org.whitebox.howlook.domain.evaluation.entity.CreatorEval;
import org.whitebox.howlook.domain.evaluation.entity.CreatorReply;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.evaluation.repository.CreatorEvalRepository;
import org.whitebox.howlook.domain.evaluation.repository.CreatorReplyRepository;
import org.whitebox.howlook.domain.member.entity.Creator;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
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
    private final CreatorReplyRepository creatorReplyReopository;

    private final MemberRepository memberRepository;

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
            creatorEval.setContent(creatorEvalRegisterDTO.getContent());
            creatorEval.setLikeCount(0L);
            creatorEval.setCommentCount(0L);

            creatorEvalRepository.save(creatorEval);
        }

        else {
            throw new LackOfLubyException();
        }
    }

    @Override
    public CreatorEvalReadDTO readByCreatorEvalId(Long creatorEvalId)
    {
        Member member = accountUtil.getLoginMember();

        Optional<CreatorEval> result = creatorEvalRepository.findById(creatorEvalId);

        CreatorEval creatorEval = result.orElseThrow();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CreatorEvalReadDTO creatorEvalReadDTO = modelMapper.map(creatorEval, CreatorEvalReadDTO.class);

        creatorEvalReadDTO.setAverageScore(0); // 크리에이터 평가글 댓글 생성전까지 임시 값.
        creatorEvalReadDTO.setCreatorEvalId(creatorEvalId);

        // 크리에이터 평가글 댓글들에 대한 점수 구하기
        Optional<List<CreatorReply>> replies = creatorReplyReopository.findBypid(creatorEvalId);
        List<CreatorReply> creatorReplies = replies.orElseThrow();

        float averScore = 0;
        Long rCount = 0L;

        for(CreatorReply r : creatorReplies)
        {
            averScore += r.getScore();
            rCount += 1;
        }

        if(averScore != 0 && rCount != 0)
            averScore = averScore/rCount;

        creatorEvalReadDTO.setAverageScore(averScore);

        return creatorEvalReadDTO;
    }

    @Override
    public List<CreatorEvalReadDTO> getCreatorEvalPage(int page,int size)
    {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Pageable pageable = PageRequest.of(page,size);

        Page<CreatorEvalReadDTO> creatorEvalPage = creatorEvalRepository.findCreatorEvalReadDTOPage(pageable);

        List<CreatorEvalReadDTO> getListFromPage = creatorEvalPage.getContent();

        List<CreatorEvalReadDTO> readDTOList = new ArrayList();

        for(int i = 0; i < getListFromPage.size(); i++) {

            CreatorEvalReadDTO creatorEvalReadDTO = getListFromPage.get(i);

            if(checkEvalHasMyReply(creatorEvalReadDTO) && !checkMyEvalPost(creatorEvalReadDTO) ) {
                readDTOList.add(creatorEvalReadDTO);
            }

            else{
                List<CreatorEvalReadDTO> temp = getCreatorEvalPage(page + 1, size);

                if(temp != null) {
                    for (int j = 0; j < temp.size(); j++)
                        readDTOList.add(temp.get(j));
                }
            }
        }

        return readDTOList;
    }

    @Override
    public CreatorEvalPageDTO getCreatorEvalWithHasMore(int page, int size)
    {
        final List<CreatorEvalReadDTO> creatorEvalPage = getCreatorEvalPage(page,size);

        if(creatorEvalPage == null || creatorEvalPage.size() == 0)
        {
            return null;
        }

        CreatorEvalPageDTO creatorEvalPageDTO = new CreatorEvalPageDTO(creatorEvalPage.get(0));

        if(creatorEvalPage.size() >= 2 && creatorEvalPage.get(0).getCreatorEvalId() != creatorEvalPage.get(1).getCreatorEvalId())
        {
            // hasMore = 1
            creatorEvalPageDTO.setHasMore(1L);
        }
        else {
            // hasMore = 0
            creatorEvalPageDTO.setHasMore(0L);
        }

        return creatorEvalPageDTO;
    }

    @Override
    public List<CreatorEvalReadDTO> getListOfUId(String userId) {
        List<CreatorEval> creatorEvals = creatorEvalRepository.findByUserId(userId);
        List<CreatorEvalReadDTO> result = new ArrayList<>();

        for (CreatorEval creatorEval : creatorEvals) {
            CreatorEvalReadDTO creatorEvalReadDTO = new CreatorEvalReadDTO().builder()
                    .creatorEvalId(creatorEval.getEvalId())
                    .mainPhotoPath(creatorEval.getMainPhotoPath()).build();
            result.add(creatorEvalReadDTO);

            // 크리에이터 평가글 댓글들에 대한 점수 구하기
            Optional<List<CreatorReply>> replies = creatorReplyReopository.findBypid(creatorEval.getEvalId());
            List<CreatorReply> creatorReplies = replies.orElseThrow();

            float averScore = 0;
            Long rCount = 0L;

            for(CreatorReply r : creatorReplies)
            {
                averScore += r.getScore();
                rCount += 1;
            }

            if(averScore != 0 && rCount != 0)
                averScore = averScore/rCount;

            creatorEvalReadDTO.setAverageScore(averScore);
//        }

        }
        return result;
    }

    public boolean checkEvalHasMyReply(CreatorEvalReadDTO creatorEvalReadDTO) {
        CreatorReply temp = creatorReplyReopository
                .findMyReplyByPostid(creatorEvalReadDTO.getCreatorEvalId(), accountUtil.getLoginMemberId());

        if (temp == null)
            return true;

        return false;
    }

    public boolean checkMyEvalPost(CreatorEvalReadDTO creatorEvalReadDTO) {
        CreatorEval creatorEval = creatorEvalRepository.findByPid(creatorEvalReadDTO.getCreatorEvalId()).get();

        if (creatorEval.getMember().getMemberId() == accountUtil.getLoginMember().getMemberId())
            return true;

        return false;
    }

    public boolean checkIAMCreator() {
        Member member = accountUtil.getLoginMember();

        if(memberRepository.getCreatorbyUID(member.getMemberId()).isPresent()) {
            return true;
        }

        return false;
    }
}