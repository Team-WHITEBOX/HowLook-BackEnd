package org.whitebox.howlook.domain.feed.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "Comment_like_History")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Comment_like_id")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentId")
    private Reply reply;

    @Builder
    public ReplyLike(Member member, Reply reply) {
        this.member = member;
        this.reply = reply;
    }
}
