package org.whitebox.howlook.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;
import org.whitebox.howlook.domain.upload.dto.PhotoDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostReaderDTO {

    private Long postId;       //게시글 ids

    UserPostInfoResponse userPostInfo;

    private Long photoCount;      //업로드한 사진 개수

    private Long likeCount;     //좋아요개수

    private Long replyCount;  //댓글개수

    private Long viewCount;       //조회수

    private String content;     //내용

    private String mainPhotoPath; //사진 경로

    private List<PhotoDTO> PhotoDTOs;

    @JsonProperty("regDate")
    private LocalDateTime registrationDate;

    @JsonProperty("modDate")
    private LocalDateTime modificationDate;

    private HashtagDTO hashtagDTO;

    private Boolean likeCheck; // 이 댓글에 좋아요를 눌렀는지 확인.

    @QueryProjection
    public PostReaderDTO(Post post){
        this.postId = post.getPostId();
        this.userPostInfo = new UserPostInfoResponse(post.getMember());
        this.photoCount = post.getPhotoCount();
        this.likeCount = post.getLikeCount();
        this.replyCount = post.getPostReplyCount();
        this.viewCount = post.getViewCount();
        this.content = post.getContent();
        this.mainPhotoPath = post.getMainPhotoPath();
        this.registrationDate = post.getRegDate();
        this.modificationDate = post.getModDate();
        this.hashtagDTO = new HashtagDTO(post.getHashtag());
        this.likeCheck = false;
    }
}