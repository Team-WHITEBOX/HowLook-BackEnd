package org.whitebox.howlook.domain.member.service;

import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.member.dto.*;

import java.util.List;

public interface MemberService {
    static class MidExistException extends Exception{
    }
    void join(MemberJoinDTO memberJoinDTO)throws MidExistException;
    boolean checkMemberId(String memberId);
    void updatePassword(UpdatePasswordRequest updatePasswordRequest);
    EditProfileResponse getEditProfile();
    void editProfile(EditProfileRequest editProfileRequest);

    void editProfilePhoto(Long feedId);
    UserProfileResponse getUserProfile(String usermid);
    UserPostInfoResponse getUserPostInfo(String usermid);

    List<FeedReaderDTO> getUserScrap(String usermid);
}
