package org.whitebox.howlook.domain.evaluation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CreatorEvalRegisterDTO {
    @Positive(message = "등록할 평가 게시글의 아이디를 입력해주세요")
    private Long creatorEvalId; // 크리에이터 평가 게시글 아이디

    @NotNull(message = "사진은 필수입니다.")
    private UploadFileDTO files;
}
