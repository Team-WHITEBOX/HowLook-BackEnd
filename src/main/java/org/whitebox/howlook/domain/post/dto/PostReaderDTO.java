package org.whitebox.howlook.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.upload.dto.PhotoDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostReaderDTO {
    @Positive
    private Long postId;       //게시글 ids

    UserPostInfoResponse userPostInfo;

    @NotNull(message = "사진은 반드시 한 장 이상 포함되므로 사진개수가 필요하옵니다.")
    @Positive
    private Long photoCount;      //업로드한 사진 개수

    private Long likeCount;     //좋아요개수

    private Long replyCount;  //댓글개수

    private Long viewCount;       //조회수

    private String content;     //내용

    @NotBlank(message = "사진이 반드시 있을 것이기 때문에 사진경로가 필요하외다.")
    private String mainPhotoPath; //사진 경로

    private List<PhotoDTO> PhotoDTOs = new ArrayList<>();

    @JsonProperty("regDate")
    private LocalDateTime registrationDate;

    @JsonProperty("modDate")
    private LocalDateTime modificationDate;

    @NotNull(message = "해시태그가 없는 게시물은 없소.")
    private HashtagDTO hashtagDTO;

    private Boolean likeCheck; // 이 댓글에 좋아요를 눌렀는지 확인.

    private Long temperature; // 온도
    private Long weather; // 날씨

    private Boolean isScrapped;

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
        this.temperature = post.getTemperature();
        this.weather = post.getWeather();
    }
}