package org.whitebox.howlook.domain.evaluation.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicInsert
@Setter
public class EvalReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId; // 평가 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member; // 작성자 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postid")
    private Evaluation evaluation; // 글 정보

    @Column(columnDefinition = "INT default 1")
    private Long score;
}