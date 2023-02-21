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
public class EvalReaderDTO {

    private Long postId;       //게시글 id
    private String mainPhotoPath; //사진 경로

    private float averageScore;

    @QueryProjection
    public EvalReaderDTO(Evaluation eval) {
        this.postId = eval.getPostId();
        this.mainPhotoPath = eval.getMainPhotoPath();
    }
}
