package org.whitebox.howlook.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.post.entity.Scrap;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByMemberAndPost(Member member, Post post);
    @Query("select s from Scrap s where s.member.memberId = :memberId")
    List<Scrap> findAllByMemberId(@Param("memberId") String memberId);
    @Query("select s from Scrap s where s.post.postId =:postId")
    List<Scrap> findAllByPostId(Long postId);
}