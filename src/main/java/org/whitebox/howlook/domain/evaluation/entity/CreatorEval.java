package org.whitebox.howlook.domain.evaluation.entity;

import lombok.Getter;
import lombok.Setter;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Table(name = "CreatorEval")
public class CreatorEval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long evalId; // 게시글에 대한 평가 아이디

    @Column(columnDefinition = "INT default 0")
    private Long likeCount; //좋아요개수

    @Column(columnDefinition = "INT default 0")
    private Long commentCount; //댓글개수

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member; // 작성자 정보

    private String content; //내용

    private String mainPhotoPath; //사진 경로
}
