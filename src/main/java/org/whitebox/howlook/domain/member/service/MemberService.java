package org.whitebox.howlook.domain.member.service;

import org.whitebox.howlook.domain.member.dto.*;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.global.config.security.dto.TokenDTO;

public interface MemberService {
    void logout(TokenDTO tokenDTO);
    Member join(MemberJoinDTO memberJoinDTO);
    boolean checkMemberId(String memberId);
    boolean checkNickName(String nickName);
    void updatePassword(UpdatePasswordRequest updatePasswordRequest);
    EditProfileResponse getEditProfile();
    void editProfile(EditProfileRequest editProfileRequest);
    void socialEditProfile(SocialEditProfileRequest socialEditProfileRequest);

    void editProfilePhoto(Long postId);
    UserProfileResponse getUserProfile(String memberId);
    UserPostInfoResponse getUserPostInfo(String memberId);

    ScrapsResponse getUserScrap(String memberId);
}
