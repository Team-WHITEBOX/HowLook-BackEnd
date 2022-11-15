package org.whitebox.howlook.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedRegisterDTO {
    private Long NPostId;       //게시글 id
    private Long PhotoCnt;      //업로드한 사진 개수
    private String Content;     //내용
    private String MainPhotoPath; //사진 경로
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
