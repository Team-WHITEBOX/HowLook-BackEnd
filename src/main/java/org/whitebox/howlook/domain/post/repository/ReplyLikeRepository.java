package org.whitebox.howlook.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.post.entity.Reply;
import org.whitebox.howlook.domain.post.entity.ReplyLike;
import org.whitebox.howlook.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    List<ReplyLike> findAllByReplyIn(List<Reply> replies);
    Optional<ReplyLike> findByMemberAndReply(Member member, Reply reply);
    @Query("select e from ReplyLike e where e.reply.replyId =:replyId AND e.member.memberId =:memberId")
    Optional<ReplyLike> findByMemberIdAndReplyId(String memberId, Long replyId);
    @Query("select e from ReplyLike e where e.reply.replyId =:replyId")
    List<ReplyLike> findByReplyId(Long replyId);
}
