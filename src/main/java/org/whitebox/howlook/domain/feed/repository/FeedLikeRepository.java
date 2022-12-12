package org.whitebox.howlook.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.FeedLike;
import org.whitebox.howlook.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    List<FeedLike> findAllByFeed(Feed feed);

    Optional<FeedLike> findByMemberAndFeed(Member member, Feed feed);

    @Query("select fl from FeedLike fl join fetch fl.member where fl.feed.NPostId = :NPostId")
    List<FeedLike> findAllWithMemberByNPostId(@Param("NPostId") Long NPostId);


}