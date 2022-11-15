package org.whitebox.howlook.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedRegisterDTO {

    //@NotNull
    private Long NPostId;       //게시글 id

    //@NotNull
    private Long PhotoCnt;      //업로드한 사진 개수

    private String Content;     //내용

    private String MainPhotoPath; //사진 경로

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
