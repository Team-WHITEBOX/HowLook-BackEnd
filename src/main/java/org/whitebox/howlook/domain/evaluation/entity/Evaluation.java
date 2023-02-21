package org.whitebox.howlook.domain.evaluation.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.post.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicInsert
@Setter

public class Evaluation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;       //게시글 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member; // 작성자 정보

    @Column(columnDefinition = "INT default 0")
    private Long likeCount;     //좋아요개수

    @Column(columnDefinition = "INT default 0")
    private Long commentCount;  //댓글개수

    private String content;     //내용

    private String mainPhotoPath; //사진 경로

    public void setMember(Member member){
        this.member = member;
    }
}
