package org.whitebox.howlook.domain.report.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;
import org.whitebox.howlook.domain.post.dto.HashtagDTO;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.upload.dto.PhotoDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReportDTO {

    private Long postId;

    private String memberId;      //원래 Member member이나 이름만 넘어가면되니 String으로 설정

    private Long photoCount;      //업로드한 사진 개수

    private Long likeCount;     //좋아요개수

    private Long postReplyCount;  //댓글개수

    private Long viewCount;       //조회수

    private String content;     //내용

    // postId를 통해 사진을 가져오는 get Method가 구현되어서 엔티티 구조 변경
    private String mainPhotoPath; //사진 경로

    //수정필요
    private List<PhotoDTO> PhotoDTOs = new ArrayList<>();

    private LocalDateTime registrationDate;

    private LocalDateTime modificationDate;

    private HashtagDTO hashtagDTO;

    private String reporterId;

    @QueryProjection
    public ReportDTO(Post post){
        this.postId = post.getPostId();
        this.memberId = post.getMember().getMemberId();
        this.photoCount = post.getPhotoCount();
        this.likeCount = post.getLikeCount();
        this.postReplyCount = post.getPostReplyCount();
        this.viewCount = post.getViewCount();
        this.content = post.getContent();
        //this.mainPhotoPath = post.getMainPhotoPath();
        this.registrationDate = post.getRegDate();
        this.modificationDate = post.getModDate();
        this.hashtagDTO = new HashtagDTO(post.getHashtag());
    }
}
