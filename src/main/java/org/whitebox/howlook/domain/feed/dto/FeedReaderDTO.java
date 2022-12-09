package org.whitebox.howlook.domain.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;
import org.whitebox.howlook.domain.upload.dto.PhotoDTO;

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

    private List<PhotoDTO> PhotoDTOs;

    @JsonProperty("regDate")
    private LocalDateTime regDate;

    @JsonProperty("modDate")
    private LocalDateTime modDate;

    private HashtagDTO hashtagDTO;

    private Boolean like_chk;

    @QueryProjection
    public FeedReaderDTO(Feed feed){
        this.NPostId = feed.getNPostId();
        this.userPostInfo = new UserPostInfoResponse(feed.getMember());
        this.PhotoCnt = feed.getPhotoCnt();
        this.LikeCount = feed.getLikeCount();
        this.CommentCount = feed.getCommentCount();
        this.ViewCnt = feed.getViewCnt();
        this.Content = feed.getContent();
        this.MainPhotoPath = feed.getMainPhotoPath();
        this.regDate = feed.getRegDate();
        this.modDate = feed.getModDate();
        this.hashtagDTO = new HashtagDTO(feed.getHashtag());
        this.like_chk = false;
    }
}
