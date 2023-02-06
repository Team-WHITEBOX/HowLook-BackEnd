package org.whitebox.howlook.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRegisterDTO {
    //@NotNull
    private Long postId;           //게시글 id
    //@NotNull
    private Long photoCount;          //업로드한 사진 개수

    private String content;         //내용

    private float latitude;         //위도
    private float longitude;        //경도

    private LocalDateTime registrationDate;

    private LocalDateTime modificationDate;

    private UploadFileDTO uploadFileDTO;    //upload 도메인에서 불러온 DTO

    //동일 도메인이지만 Hashtag DTO를 불러와 게시글 입력시 같이 사용
    private HashtagDTO hashtagDTO;
}
