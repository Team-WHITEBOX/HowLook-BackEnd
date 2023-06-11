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
    private String content;

    @NotNull(message = "사진은 필수입니다.")
    private UploadFileDTO files;
}
