package org.whitebox.howlook.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.whitebox.howlook.domain.post.entity.Reply;
import org.whitebox.howlook.domain.member.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long>{
    @Query("select r from Reply r where r.post.postId = :postId")
    List<Reply> listOfpost(Long postId);

    @Query("select r.replyId from Reply r where r.post.postId = :postId")
    List<Long> listOfReplyId(Long postId);
//  @Query(value = "select r from Reply r join fetch r.post where r.CommentId = :CommentId")
//  Optional<Reply> findWithPostAndMemberById(@Param("CommentId") Long CommentId);
}