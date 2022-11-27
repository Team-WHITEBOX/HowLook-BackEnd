package org.whitebox.howlook.domain.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedReaderDTO {

    private Long NPostId;       //게시글 id

    //private String UserId;      //string인 user의 id

    UserPostInfoResponse userPostInfo;

    private Long PhotoCnt;      //업로드한 사진 개수

    private Long LikeCount;     //좋아요개수

    private Long CommentCount;  //댓글개수

    private Long ViewCnt;       //조회수

    private String Content;     //내용

    private String MainPhotoPath; //사진 경로

    private List<String> PhotoPaths;

    @JsonProperty("regDate")
    private LocalDateTime regDate;

    @JsonProperty("modDate")
    private LocalDateTime modDate;
}
