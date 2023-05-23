package org.whitebox.howlook.domain.creator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CreatorEvalRegisterDTO {
    @Positive
    private Evaluation evaluation; // 평가 게시글 아이디

    @NotBlank(message = "게시글에 대한 평가를 남겨주세요")
    @Size(min = 1, max = 1000, message = "댓글은 1문자 이상 1000문자 이하여야 합니다.")
    private String content; // 게시물에 대한 크리에이터 평가글
}
