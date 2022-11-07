package org.whitebox.howlook.domain.member.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.transaction.Transactional;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {
    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid and m.social = false")
    Optional<Member> getWithRoles(String mid);

    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByMid(String mid);

    @Modifying
    @Transactional
    @Query("update Member m set m.mpw =:mpw where m.mid = :mid ")
    void updatePassword(@Param("mpw") String password,@Param("mid") String mid);
}
