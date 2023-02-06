package org.whitebox.howlook.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.post.entity.Post;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryQuerydsl {
    @Query("select f from Post f where f.member.memberId = :memberId")
    List<Post> findByMemberId(String memberId);
    @Query("select f from Post f where f.postId= :postId")
    Post findByPostId(Long postId);

    @Query("select f.member.memberId from Post f where f.postId = :postId")
    String findMemberIdByPostId(Long postId);
}