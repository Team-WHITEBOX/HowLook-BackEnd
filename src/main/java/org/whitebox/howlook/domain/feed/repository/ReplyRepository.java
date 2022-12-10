package org.whitebox.howlook.domain.feed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.whitebox.howlook.domain.feed.entity.Reply;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long>{
    @Query("select r from Reply r where r.feed.NPostId = :NpostId")
    List<Reply> listOfFeed(Long NpostId);

    @Query("select r.ReplyId from Reply r where r.feed.NPostId = :NpostId")
    List<Long> listOfReplyId(Long NpostId);
//  @Query(value = "select r from Reply r join fetch r.feed where r.CommentId = :CommentId")
//  Optional<Reply> findWithPostAndMemberById(@Param("CommentId") Long CommentId);
}
