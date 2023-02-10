package org.whitebox.howlook.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.dto.SimplePostDTO;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.post.entity.Scrap;
import org.whitebox.howlook.domain.post.repository.PostRepository;
import org.whitebox.howlook.domain.post.repository.ScrapRepository;
import org.whitebox.howlook.domain.member.dto.*;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.entity.MemberRole;
import org.whitebox.howlook.domain.member.exception.AccountMismatchException;
import org.whitebox.howlook.domain.member.exception.memberIdExistException;
import org.whitebox.howlook.domain.member.exception.PasswordEqualWithOldException;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.domain.upload.service.UploadService;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;

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
    private final PostRepository postRepository;
    private final ScrapRepository scrapRepository;
    private final PasswordEncoder passwordEncoder;
    private final UploadService uploadService; // 업로드 서비스
    @Override
    public void join(MemberJoinDTO memberJoinDTO) {
        String memberId = memberJoinDTO.getMemberId();
        boolean exist = memberRepository.existsById(memberId);
        if(exist){
            throw new memberIdExistException();
        }
        Member member = modelMapper.map(memberJoinDTO,Member.class);
        member.updatePassword(passwordEncoder.encode(memberJoinDTO.getMemberPassword()));
        member.addRole(MemberRole.USER);

        log.info("========================");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);
    }
    @Transactional(readOnly = true)
    @Override
    public boolean checkMemberId(String memberId) {
        return !memberRepository.existsById(memberId);
    }

    @Override
    public boolean checkNickName(String nickName) {
        return !memberRepository.existsByNickName(nickName);
    }

    @Transactional
    @Override
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        log.info("member service");
        final Member member = accountUtil.getLoginMember();
        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), member.getMemberPassword())) {
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

//        log.info("프로필 수정");
//        if (memberRepository.existsById(editProfileRequest.getMemberId())
//                && !member.getMemberId().equals(editProfileRequest.getMemberId())) {
//            log.info("예외");
//            throw new UsernameAlreadyExistException();
//        }
        log.info("수정");
        member.updateNickName(editProfileRequest.getMemberNickName());
        member.updateHeight(editProfileRequest.getMemberHeight());
        member.updateWeight(editProfileRequest.getMemberWeight());
        member.updatePhone(editProfileRequest.getMemberPhone());
        memberRepository.save(member);
    }

    @Override
    public void socialEditProfile(SocialEditProfileRequest socialEditProfileRequest) {
        final Member member = accountUtil.getLoginMember();

//        log.info("프로필 수정");
//        if (memberRepository.existsById(socialEditProfileRequest.getMemberId())
//                && !member.getMemberId().equals(socialEditProfileRequest.getMemberId())) {
//            log.info("예외");
//            throw new UsernameAlreadyExistException();
//        }
        log.info("수정");
        member.updateName(socialEditProfileRequest.getMemberName());
        member.updateNickName(socialEditProfileRequest.getMemberNickName());
        member.updateHeight(socialEditProfileRequest.getMemberHeight());
        member.updateWeight(socialEditProfileRequest.getMemberWeight());
        member.updatePhone(socialEditProfileRequest.getMemberPhone());
        member.updateBirthDay(socialEditProfileRequest.getMemberBirthDay());
        memberRepository.save(member);
    }

    @Override
    public void editProfilePhoto(Long postId) {
        final Member member = accountUtil.getLoginMember();

        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        member.updateProfilePhotoId(post.getMainPhotoPath());
        memberRepository.save(member);
    }

    @Override
    public UserProfileResponse getUserProfile(String memberId) {
        final String loginMemberId = accountUtil.getLoginMemberId();

        final UserProfileResponse result = memberRepository.findUserProfileByMemberIdAndTargetMemberId(loginMemberId,memberId)
                .orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));

        final List<Post> posts = postRepository.findByMemberId(memberId);
        log.info(posts);
        List<SimplePostDTO> SimplePostDTOs = posts.stream().map(post -> new SimplePostDTO(post.getPostId(),post.getMainPhotoPath())).collect(Collectors.toList());

        result.setMemberPosts(SimplePostDTOs);
        return result;
    }

    @Override
    public UserPostInfoResponse getUserPostInfo(String memberId) {
        final UserPostInfoResponse result = memberRepository.findUserPostInfoByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
        return result;
    }

    @Override
    public List<PostReaderDTO> getUserScrap(String memberId) {
        final List<Scrap> scraps = scrapRepository.findAllByMemberId(memberId);
        final List<PostReaderDTO> postReaderDTOS = scraps.stream().map(scrap -> new PostReaderDTO(scrap.getPost())).collect(Collectors.toList());
        postReaderDTOS.forEach(postReaderDTO -> postReaderDTO.setPhotoDTOs(uploadService.getPhotoData(postReaderDTO.getPostId())));
        return postReaderDTOS;
    }
}