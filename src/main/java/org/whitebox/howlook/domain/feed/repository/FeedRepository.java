package org.whitebox.howlook.domain.feed.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.feed.entity.Feed;

import java.util.List;


public interface FeedRepository extends JpaRepository<Feed, Long> {
    //public List<Feed> getFeedByUserID(String UserID);

    private final JPAQueryFactory queryFactory;
    @Query("select f from Feed f where f.member.mid = :mid")
    List<Feed> findByMid(String mid);

    final List<Feed> posDtos = queryFactory
            .select
}