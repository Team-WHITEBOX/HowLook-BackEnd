package org.whitebox.howlook.domain.member.service;

import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.member.dto.*;

import java.util.List;

public interface MemberService {
    static class MidExistException extends Exception{
    }
    void join(MemberJoinDTO memberJoinDTO)throws MidExistException;
    void updatePassword(UpdatePasswordRequest updatePasswordRequest);
    EditProfileResponse getEditProfile();
    void editProfile(EditProfileRequest editProfileRequest);
    UserProfileResponse getUserProfile(String usermid);
    UserPostInfoResponse getUserPostInfo(String usermid);

    List<FeedReaderDTO> getUserScrap(String usermid);
}
