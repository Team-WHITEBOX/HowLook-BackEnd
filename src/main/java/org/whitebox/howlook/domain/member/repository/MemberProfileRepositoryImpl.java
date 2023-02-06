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
    public Optional<UserProfileResponse> findUserProfileByMemberIdAndTargetMemberId(String loginMemberId, String memberId){
        return Optional.ofNullable(queryFactory
                .select(new QUserProfileResponse(
                        member.memberId,
                        member.nickName,
                        member.height,
                        member.weight,
                        member.profilePhoto,
                        member.memberId.eq(loginMemberId)))
                .from(member)
                .where(member.memberId.eq(memberId))
                .fetchOne());
    }

    @Override
    public Optional<UserPostInfoResponse> findUserPostInfoByMemberId(String memberId) {
        return Optional.ofNullable(queryFactory
                .select(new QUserPostInfoResponse(
                        member.memberId,
                        member.nickName,
                        member.height,
                        member.weight,
                        member.profilePhoto))
                .from(member)
                .where(member.memberId.eq(memberId))
                .fetchOne());
//        return Optional.empty();
    }
}
