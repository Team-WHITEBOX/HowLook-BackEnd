package org.whitebox.howlook.domain.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.post.entity.Post;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {
    private Post post;
    private String path; // 사진이 저장될 경로
}
