package org.whitebox.howlook.domain.upload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {
    private Long NPostId; // 게시글 아이디
    private String Path; // 사진이 저장될 경로
}
