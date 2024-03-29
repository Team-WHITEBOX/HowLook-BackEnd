package org.whitebox.howlook.domain.evaluation.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.whitebox.howlook.domain.evaluation.entity.CreatorEval;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatorEvalReadDTO {

    private Long creatorEvalId;   // 크리에이터 평가글 id
    private String mainPhotoPath; //사진 경로

    private float averageScore;  // 글의 평균 점수

    @QueryProjection
    public CreatorEvalReadDTO(CreatorEval creatorEval) {
        this.creatorEvalId = creatorEval.getEvalId();
        this.mainPhotoPath = creatorEval.getMainPhotoPath();
    }
}
