package org.whitebox.howlook.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.Scrap;
import org.whitebox.howlook.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByMemberAndFeed(Member member, Feed feed);
    @Query("select s from Scrap s where s.member.mid = :mid")
    List<Scrap> findAllByMid(String mid);
    @Query("select s from Scrap s where s.feed.NPostId =:NpostId")
    List<Scrap> findAllByNpostId(Long NpostId);
}
