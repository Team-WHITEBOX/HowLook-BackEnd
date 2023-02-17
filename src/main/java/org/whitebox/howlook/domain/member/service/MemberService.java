package org.whitebox.howlook.domain.member.service;

import org.whitebox.howlook.domain.member.dto.*;
import org.whitebox.howlook.domain.post.dto.SimplePostDTO;

import java.util.List;

public interface MemberService {
    void join(MemberJoinDTO memberJoinDTO);
    boolean checkMemberId(String memberId);
    boolean checkNickName(String nickName);
    void updatePassword(UpdatePasswordRequest updatePasswordRequest);
    EditProfileResponse getEditProfile();
    void editProfile(EditProfileRequest editProfileRequest);
    void socialEditProfile(SocialEditProfileRequest socialEditProfileRequest);

    void editProfilePhoto(Long postId);
    UserProfileResponse getUserProfile(String memberId);
    UserPostInfoResponse getUserPostInfo(String memberId);

    List<SimplePostDTO> getUserScrap(String memberId);
}
