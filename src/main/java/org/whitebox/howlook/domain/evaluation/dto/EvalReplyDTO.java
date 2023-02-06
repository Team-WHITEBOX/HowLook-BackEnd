package org.whitebox.howlook.domain.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvalReplyDTO {
    private Long replyId;       // 평가 id
    private Double score;       // 평가 점수
    private Long postId;           // 평가받을 글 id
}
