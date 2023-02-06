package org.whitebox.howlook.domain.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.post.entity.Post;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface postToTournaRepository extends JpaRepository<Post,Long>{
    @Query("select max(f.likeCount),f.member.memberId from Post f where f.regDate = :m_date GROUP BY f.regDate,f.member.memberId ORDER BY max(f.likeCount) DESC")
    List<Map<Long,Long>> FindByDate(LocalDate m_date);
}
