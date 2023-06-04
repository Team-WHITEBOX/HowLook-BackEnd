package org.whitebox.howlook.domain.evaluation.entity;

import lombok.Data;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CreatorEval")
public class CreatorEval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long evalId; // 게시글에 대한 평가 아이디

    @ManyToOne(fetch =  FetchType.LAZY)
    private Evaluation evaluation; // 평가 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member; // creator 정보

    private int score; // 평가 점수

    private String content; // 내용

    public void ChangeContent(String changeText) {
        content = changeText;
    }
}
