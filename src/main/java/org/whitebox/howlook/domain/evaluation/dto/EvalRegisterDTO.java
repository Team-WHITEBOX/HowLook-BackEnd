package org.whitebox.howlook.domain.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvalRegisterDTO {

    //@NotNull
    private Long postId;           //게시글 id

    private UploadFileDTO files;
}
