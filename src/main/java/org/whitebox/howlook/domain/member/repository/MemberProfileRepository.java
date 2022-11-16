package org.whitebox.howlook.domain.member.repository;
;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;
import org.whitebox.howlook.domain.member.dto.UserProfileResponse;

import java.util.Optional;

public interface MemberProfileRepository {
    Optional<UserProfileResponse> findUserProfileByMidAndTargetUsermid(String loginMemberId, String usermid);
    Optional<UserPostInfoResponse> findUserPostInfoByMid(String usermid);
}
