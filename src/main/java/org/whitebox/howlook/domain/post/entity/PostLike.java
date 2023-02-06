package org.whitebox.howlook.domain.post.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "Post_likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public PostLike(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}