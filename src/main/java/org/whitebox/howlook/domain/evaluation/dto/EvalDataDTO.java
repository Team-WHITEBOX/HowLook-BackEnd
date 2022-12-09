package org.whitebox.howlook.domain.evaluation.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.feed.entity.Feed;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvalDataDTO {
    private float averageScore; // 평균점수

    private float maleScore; // 남성 평균점수

    private float femaleScore; // 여성 평균점수

    private float maxScore; // 최고 점수

    private float minScore; // 최저 점수
    
    private Long replyCount; // 평가 개수

    private Long maleCount; // 남자 평가 개수

    private Long femaleCount; // 여자 평가 개수

    @Builder.Default
    private float[] maleScores = new float[5];

    @Builder.Default
    private float[] femaleScores = new float[5];

    @Builder.Default
    private Long[] maleCounts = new Long[5];

    @Builder.Default
    private Long[] femaleCounts = new Long[5];
}
