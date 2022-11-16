package org.whitebox.howlook.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.whitebox.howlook.domain.member.dto.QUserPostInfoResponse;
import org.whitebox.howlook.domain.member.dto.QUserProfileResponse;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;
import org.whitebox.howlook.domain.member.dto.UserProfileResponse;

import java.util.Optional;

import static org.whitebox.howlook.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class MemberProfileRepositoryImpl implements MemberProfileRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<UserProfileResponse> findUserProfileByMidAndTargetUsermid(String loginMemberId, String usermid){
        return Optional.ofNullable(queryFactory
                .select(new QUserProfileResponse(
                        member.mid,
                        member.nickName,
                        member.height,
                        member.weight,
                        member.profilePhotoId,
                        member.mid.eq(loginMemberId)))
                .from(member)
                .where(member.mid.eq(usermid))
                .fetchOne());
    }

    @Override
    public Optional<UserPostInfoResponse> findUserPostInfoByMid(String usermid) {
        return Optional.ofNullable(queryFactory
                .select(new QUserPostInfoResponse(
                        member.mid,
                        member.nickName,
                        member.height,
                        member.weight,
                        member.profilePhotoId))
                .from(member)
                .where(member.mid.eq(usermid))
                .fetchOne());
//        return Optional.empty();
    }
}
