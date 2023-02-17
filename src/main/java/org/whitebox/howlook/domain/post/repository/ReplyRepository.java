package org.whitebox.howlook.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.post.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long>{
    @Query("select r from Reply r where r.post.postId = :postId")
    List<Reply> listOfpost(Long postId);

    @Query("select r.replyId from Reply r where r.post.postId = :postId")
    List<Long> listOfReplyId(Long postId);
//  @Query(value = "select r from Reply r join fetch r.post where r.CommentId = :CommentId")
//  Optional<Reply> findWithPostAndMemberById(@Param("CommentId") Long CommentId);
}