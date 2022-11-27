package org.whitebox.howlook.domain.upload.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadFileDTO {
    private Long NPostId; // 게시글 ID
    private List<MultipartFile> files; // 여러 장을 담을 파일 리스트

}
