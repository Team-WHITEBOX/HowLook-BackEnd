package org.whitebox.howlook.domain.evaluation.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.whitebox.howlook.domain.evaluation.entity.CreatorEval;
import org.whitebox.howlook.domain.member.entity.Member;

@Getter
@Setter
@NoArgsConstructor
public class CreatorEvalReadDTO {
    String NickName; // 평가자의 이름

    int point; // 평가 점수

    String content; // 한줄평

    @QueryProjection
    public CreatorEvalReadDTO(CreatorEval creatorEval) {
        this.NickName =  creatorEval.getMember().getNickName();
        this.point = creatorEval.getScore();
        this.content = creatorEval.getContent();
    }
}
