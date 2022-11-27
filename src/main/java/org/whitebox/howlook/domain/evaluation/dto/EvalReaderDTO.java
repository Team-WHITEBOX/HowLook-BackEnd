package org.whitebox.howlook.domain.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvalReaderDTO {

    private Long NPostId;       //게시글 id

    private Long PhotoCnt;      //업로드한 사진 개수

    UserPostInfoResponse userPostInfo;

    private Long LikeCount;     //좋아요개수

    private Long CommentCount;  //댓글개수

    private String Content;     //내용

    private String MainPhotoPath; //사진 경로

    @JsonProperty("regDate")
    private LocalDateTime regDate;

    @JsonProperty("modDate")
    private LocalDateTime modDate;
}
