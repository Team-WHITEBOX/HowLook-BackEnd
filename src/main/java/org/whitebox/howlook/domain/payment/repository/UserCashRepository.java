package org.whitebox.howlook.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.payment.entity.PaymentInfo;
import org.whitebox.howlook.domain.payment.entity.UserCash;

import java.util.Optional;

@Repository
public interface UserCashRepository extends JpaRepository <UserCash,Long> {

    @Query("select u from UserCash u where u.member = :member")
    UserCash findByMember(Member member);

    @Query("select u from UserCash u where u.member.memberId = :userId")
    UserCash findByMemberId(String userId);
}
