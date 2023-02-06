package org.whitebox.howlook.domain.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvalReaderDTO {

    private Long postId;       //게시글 id

    UserPostInfoResponse userPostInfo;

    private Long likeCount;     //좋아요개수

    private Long commentCount;  //댓글개수

    private String content;     //내용

    private String mainPhotoPath; //사진 경로

    @JsonProperty("regDate")
    private LocalDateTime regDate;

    @JsonProperty("modDate")
    private LocalDateTime modDate;

    private float averageScore;

    @QueryProjection
    public EvalReaderDTO(Evaluation eval) {
        this.postId = eval.getPostId();
        this.userPostInfo = new UserPostInfoResponse(eval.getMember());
        this.likeCount = eval.getLikeCount();
        this.commentCount = eval.getCommentCount();
        this.content = eval.getContent();
        this.mainPhotoPath = eval.getMainPhotoPath();
        this.regDate = eval.getRegDate();
        this.modDate = eval.getModDate();
    }
}
