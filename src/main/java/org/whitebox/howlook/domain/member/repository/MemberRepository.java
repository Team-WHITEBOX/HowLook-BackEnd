package org.whitebox.howlook.domain.member.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.transaction.Transactional;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String>,MemberProfileRepository {
    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.memberId = :memberId and m.social = false")
    Optional<Member> getWithRoles(@Param("memberId") String memberId);

    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByMemberId(@Param("memberId") String memberId);

    @Modifying
    @Transactional
    @Query("update Member m set m.memberPassword =:memberPassword where m.memberId = :memberId ")
    void updatePassword(@Param("memberPassword") String password,@Param("memberId") String memberId);
    
    boolean existsByNickName(String nickName);
}
