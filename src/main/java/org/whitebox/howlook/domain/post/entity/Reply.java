package org.whitebox.howlook.domain.post.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Table(name = "Reply")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicInsert
public class Reply extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String content;

//    public Long photoId; // 프로필ID
    @ColumnDefault("0")
    private Long parentId;

    private Long likeCount;

    public void changeText(String changedText) {
        this.content = changedText;
    }   //변경된 텍스트로 수정하는 함수

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }
}