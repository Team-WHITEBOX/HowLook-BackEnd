package org.whitebox.howlook.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.Scrap;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;
import org.whitebox.howlook.domain.feed.repository.ScrapRepository;
import org.whitebox.howlook.domain.member.dto.*;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.entity.MemberRole;
import org.whitebox.howlook.domain.member.exception.AccountMismatchException;
import org.whitebox.howlook.domain.member.exception.PasswordEqualWithOldException;
import org.whitebox.howlook.domain.member.exception.UsernameAlreadyExistException;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.whitebox.howlook.global.error.ErrorCode.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final AccountUtil accountUtil;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    private final ScrapRepository scrapRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {
        String mid = memberJoinDTO.getMid();
        boolean exist = memberRepository.existsById(mid);
        if(exist){
            throw new MidExistException();
        }
        Member member = modelMapper.map(memberJoinDTO,Member.class);
        member.updatePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        member.addRole(MemberRole.USER);

        log.info("========================");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);
    }

    @Transactional
    @Override
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        log.info("member service");
        final Member member = accountUtil.getLoginMember();
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), member.getMpw())) {
            throw new AccountMismatchException();
        }
        if (updatePasswordRequest.getOldPassword().equals(updatePasswordRequest.getNewPassword())) {
            throw new PasswordEqualWithOldException();
        }
        final String encryptedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        member.updatePassword(encryptedPassword);
        memberRepository.save(member);
    }

    @Override
    public EditProfileResponse getEditProfile() {
        final Member member = accountUtil.getLoginMember();
        return new EditProfileResponse(member);
    }

    @Override
    public void editProfile(EditProfileRequest editProfileRequest) {
        final Member member = accountUtil.getLoginMember();

        log.info("프로필 수정");
        if (memberRepository.existsById(editProfileRequest.getMemberId())
                && !member.getMid().equals(editProfileRequest.getMemberId())) {
            log.info("예외");
            throw new UsernameAlreadyExistException();
        }
        log.info("수정");
        member.updateNickName(editProfileRequest.getMemberNickName());
        member.updateHeight(editProfileRequest.getMemberHeight());
        member.updateWeight(editProfileRequest.getMemberWeight());
        member.updatePhone(editProfileRequest.getMemberPhone());
        memberRepository.save(member);
    }

    @Override
    public UserProfileResponse getUserProfile(String usermid) {
        final String loginMemberId = accountUtil.getLoginMemberId();

        final UserProfileResponse result = memberRepository.findUserProfileByMidAndTargetUsermid(loginMemberId,usermid)
                .orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));

        final List<Feed> feeds = feedRepository.findByMid(usermid);
        log.info(feeds);
        List<FeedReaderDTO> feedReaderDTOs = feeds.stream().map(feed -> new FeedReaderDTO(feed)).collect(Collectors.toList());

        result.setMemberFeeds(feedReaderDTOs);
        return result;
    }

    @Override
    public UserPostInfoResponse getUserPostInfo(String usermid) {
        final UserPostInfoResponse result = memberRepository.findUserPostInfoByMid(usermid)
                .orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
        return result;
    }

    @Override
    public List<FeedReaderDTO> getUserScrap(String usermid) {
        final List<Scrap> scraps = scrapRepository.findAllByMid(usermid);
        final List<FeedReaderDTO> feedReaderDTOs = scraps.stream().map(scrap -> new FeedReaderDTO(scrap.getFeed())).collect(Collectors.toList());

        return feedReaderDTOs;
    }
}
