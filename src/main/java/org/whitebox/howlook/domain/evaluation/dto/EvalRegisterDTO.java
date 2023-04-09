package org.whitebox.howlook.domain.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvalRegisterDTO {
    //private Long postId; 안써서 삭제

    @NotNull(message = "사진은 필수입니다.")
    private UploadFileDTO files;
}
