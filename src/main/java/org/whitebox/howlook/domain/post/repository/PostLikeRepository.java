package org.whitebox.howlook.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.post.entity.PostLike;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findAllByPost(Post post);

    Optional<PostLike> findByMemberAndPost(Member member, Post post);

    @Query("select pl from PostLike pl join fetch pl.member where pl.post.postId = :postId")
    List<PostLike> findAllWithMemberByPostId(@Param("postId") Long postId);

}