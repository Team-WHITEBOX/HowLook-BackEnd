package org.whitebox.howlook.domain.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EvalDataDTO {
    private float averageScore;

    private float maleScore;

    private float femaleScore;

    private float maxScore;

    private float minScore;
    
    private Long replyCount;

    private Long maleCount;

    private Long femaleCount;

    private float[] maleScores;
    private float[] femaleScores;

    private Long[] maleCounts;
    private Long[] femaleCounts;

    public EvalDataDTO()
    {
        averageScore = 0; // 평균점수
        maleScore = 0; // 남성 평균점수
        femaleScore = 0; // 여성 평균점수

        maxScore = 0; // 최고 점수
        minScore = 0; // 최저 점수
        replyCount = 0L; // 평가 개수

        maleCount = 0L; // 남자 평가 개수
        femaleCount = 0L; // 여자 평가 개수

        maleScores = new float[5];
        femaleScores = new float[5];

        maleCounts = new Long[5];
        femaleCounts = new Long[5];
    }
}
