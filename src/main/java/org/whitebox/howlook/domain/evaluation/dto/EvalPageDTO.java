package org.whitebox.howlook.domain.evaluation.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvalPageDTO {

    private Long postId;       //게시글 id
    private String mainPhotoPath; //사진 경로

    private float averageScore;

    private Long hasMore;

    @QueryProjection
    public EvalPageDTO(Evaluation eval) {
        this.postId = eval.getPostId();
        this.mainPhotoPath = eval.getMainPhotoPath();
    }

    public EvalPageDTO(EvalReaderDTO evalReaderDTO)
    {
        postId = evalReaderDTO.getPostId();
        mainPhotoPath = evalReaderDTO.getMainPhotoPath();
        averageScore = evalReaderDTO.getAverageScore();
        hasMore = 0L;
    }
}