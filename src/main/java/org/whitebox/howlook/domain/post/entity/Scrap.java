package org.whitebox.howlook.domain.post.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @Builder
    public Scrap(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}
