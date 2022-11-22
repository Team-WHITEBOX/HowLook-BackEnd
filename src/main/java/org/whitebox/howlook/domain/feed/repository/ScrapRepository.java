package org.whitebox.howlook.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.Scrap;
import org.whitebox.howlook.domain.member.entity.Member;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByMemberAndFeed(Member member, Feed feed);
}
