package org.whitebox.howlook.domain.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvalDataDTO {
    private float averageScore = 0; // 평균점수

    private float maleScore = 0; // 남성 평균점수

    private float femaleScore = 0; // 여성 평균점수

    private float maxScore = 0; // 최고 점수

    private float minScore = 0; // 최저 점수
    
    private Long replyCount = 0L; // 평가 개수

    private Long maleCount = 0L; // 남자 평가 개수

    private Long femaleCount = 0L; // 여자 평가 개수
}
