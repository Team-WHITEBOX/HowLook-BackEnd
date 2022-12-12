package org.whitebox.howlook.domain.feed.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.QFeedReaderDTO;
import org.whitebox.howlook.domain.feed.entity.Reply;
import org.whitebox.howlook.domain.feed.entity.ReplyLike;
import org.whitebox.howlook.domain.member.entity.Member;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static org.whitebox.howlook.domain.feed.entity.QFeed.feed;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    List<ReplyLike> findAllByReplyIn(List<Reply> replies);
    Optional<ReplyLike> findByMemberAndReply(Member member, Reply reply);
    @Query("select e from ReplyLike e where e.reply.ReplyId =:replyId AND e.member.mid =:mid")
    Optional<ReplyLike> findByMidAndReplyId(String mid, Long replyId);
    @Query("select e from ReplyLike e where e.reply.ReplyId =:replyId")
    List<ReplyLike> findByReplyId(Long replyId);
}
