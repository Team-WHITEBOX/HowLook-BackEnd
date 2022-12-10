package org.whitebox.howlook.domain.feed.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.feed.entity.Feed;

import java.util.List;


public interface FeedRepository extends JpaRepository<Feed, Long>,FeedRepositoryQuerydsl {
    @Query("select f from Feed f where f.member.mid = :mid")
    List<Feed> findByMid(String mid);
    @Query("select f from Feed f where f.NPostId= :pid")
    Feed findByPid(Long pid);

    @Query("select f.member.mid from Feed f where f.NPostId = :NPostId")
    String findMidByNPostId(Long NPostId);
}