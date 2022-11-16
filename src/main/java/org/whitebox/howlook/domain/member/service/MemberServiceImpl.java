package org.whitebox.howlook.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.member.dto.EditProfileRequest;
import org.whitebox.howlook.domain.member.dto.EditProfileResponse;
import org.whitebox.howlook.domain.member.dto.MemberJoinDTO;
import org.whitebox.howlook.domain.member.dto.UpdatePasswordRequest;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.entity.MemberRole;
import org.whitebox.howlook.domain.member.exception.AccountMismatchException;
import org.whitebox.howlook.domain.member.exception.PasswordEqualWithOldException;
import org.whitebox.howlook.domain.member.exception.UsernameAlreadyExistException;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.global.util.AccountUtil;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final AccountUtil accountUtil;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
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

        if (memberRepository.existsById(editProfileRequest.getMemberId())
                && !member.getMid().equals(editProfileRequest.getMemberId())) {
            throw new UsernameAlreadyExistException();
        }

    //    updateMemberProfile(member, editProfileRequest);
    }

}
