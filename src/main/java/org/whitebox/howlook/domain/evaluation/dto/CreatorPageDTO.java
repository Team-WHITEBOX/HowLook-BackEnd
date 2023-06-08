package org.whitebox.howlook.domain.evaluation.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.evaluation.entity.CreatorEval;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatorPageDTO {
    private Long creatorEvalId;    // 게시글 id
    private String mainPhotoPath; // 사진 경로
    private float averageScore; // 평균 점수
    private Long hasMore; // 이거 뭐야?

    @QueryProjection
    public CreatorPageDTO(CreatorEval creatorEval) {
        this.creatorEvalId = creatorEval.getEvalId();
        this.mainPhotoPath = creatorEval.getMainPhotoPath();
    }

    public CreatorPageDTO(CreatorEvalReadDTO creatorEvalReadDTO)
    {
        creatorEvalId = creatorEvalReadDTO.getCreatorEvalId();
        mainPhotoPath = creatorEvalReadDTO.getMainPhotoPath();
        averageScore = creatorEvalReadDTO.getAverageScore();
        hasMore = 0L;
    }
}