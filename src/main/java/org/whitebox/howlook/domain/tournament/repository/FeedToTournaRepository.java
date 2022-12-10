package org.whitebox.howlook.domain.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.feed.entity.Feed;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FeedToTournaRepository extends JpaRepository<Feed,Long>{
    @Query("select max(f.LikeCount),f.member.mid from Feed f where f.regDate = :m_date GROUP BY f.regDate,f.member.mid ORDER BY max(f.LikeCount) DESC")
    List<Map<Long,Long>> FindByDate(LocalDate m_date);
}
