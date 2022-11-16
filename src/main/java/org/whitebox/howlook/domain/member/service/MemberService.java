package org.whitebox.howlook.domain.member.service;

import org.whitebox.howlook.domain.member.dto.*;

public interface MemberService {
    static class MidExistException extends Exception{
    }
    void join(MemberJoinDTO memberJoinDTO)throws MidExistException;
    void updatePassword(UpdatePasswordRequest updatePasswordRequest);
    EditProfileResponse getEditProfile();
    void editProfile(EditProfileRequest editProfileRequest);
    UserProfileResponse getUserProfile(String usermid);
    UserPostInfoResponse getUserPostInfo(String usermid);
}
