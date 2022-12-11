package org.whitebox.howlook.domain.member.service;

import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.member.dto.*;

import java.util.List;

public interface MemberService {
    void join(MemberJoinDTO memberJoinDTO);
    boolean checkMemberId(String memberId);
    boolean checkNickName(String nickName);
    void updatePassword(UpdatePasswordRequest updatePasswordRequest);
    EditProfileResponse getEditProfile();
    void editProfile(EditProfileRequest editProfileRequest);
    void socialEditProfile(SocialEditProfileRequest socialEditProfileRequest);

    void editProfilePhoto(Long feedId);
    UserProfileResponse getUserProfile(String usermid);
    UserPostInfoResponse getUserPostInfo(String usermid);

    List<FeedReaderDTO> getUserScrap(String usermid);
}
