package org.whitebox.howlook.domain.Reply.entity;

import lombok.*;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Table(name = "reply_like")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Comment_like_Id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Reply r;
}