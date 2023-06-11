package org.whitebox.howlook.domain.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatorReplyDTO {

    @NotNull(message = "평가 점수는 필수입니다.")
    private Double score;       // 평가 점수

    @NotNull(message = "postId는 필수입니다.")
    private Long postId;           // 평가받을 글 id
    @NotNull(message = "리뷰 텍스트는 필수입니다.")
    private String review;           // 패션 리뷰 텍스트
}