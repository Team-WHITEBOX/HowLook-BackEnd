package org.whitebox.howlook.domain.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedReaderDTO {

    //@JsonProperty("npost-id")
    private Long NPostId;       //게시글 id

    //@JsonProperty("photo-cnt")
    private Long PhotoCnt;      //업로드한 사진 개수

    //@JsonProperty("like-count")
    private Long LikeCount;     //좋아요개수

    //@JsonProperty("comment-count")
    private Long CommentCount;  //댓글개수

    //@JsonProperty("view-cnt")
    private Long ViewCnt;       //조회수

    //@JsonProperty("content")
    private String Content;     //내용

    //@JsonProperty("mainphoto-path")
    private String MainPhotoPath; //사진 경로

    @JsonProperty("regDate")
    private LocalDateTime regDate;

    @JsonProperty("modDate")
    private LocalDateTime modDate;

    //this.NPostId =
}
