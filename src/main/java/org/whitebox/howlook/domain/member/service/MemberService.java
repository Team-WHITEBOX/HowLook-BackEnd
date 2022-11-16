package org.whitebox.howlook.domain.member.service;

import org.whitebox.howlook.domain.member.dto.EditProfileRequest;
import org.whitebox.howlook.domain.member.dto.EditProfileResponse;
import org.whitebox.howlook.domain.member.dto.MemberJoinDTO;
import org.whitebox.howlook.domain.member.dto.UpdatePasswordRequest;

public interface MemberService {
    static class MidExistException extends Exception{
    }
    void join(MemberJoinDTO memberJoinDTO)throws MidExistException;
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest);
    public EditProfileResponse getEditProfile();
    public void editProfile(EditProfileRequest editProfileRequest);
}
