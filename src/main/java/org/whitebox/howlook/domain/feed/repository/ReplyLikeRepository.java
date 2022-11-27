package org.whitebox.howlook.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.feed.entity.Reply;
import org.whitebox.howlook.domain.feed.entity.ReplyLike;
import org.whitebox.howlook.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    List<ReplyLike> findAllByReplyIn(List<Reply> replies);

    Optional<ReplyLike> findByMemberAndReply(Member member, Reply reply);
}
