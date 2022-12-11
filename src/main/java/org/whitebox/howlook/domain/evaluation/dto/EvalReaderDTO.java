package org.whitebox.howlook.domain.evaluation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.feed.dto.HashtagDTO;
import org.whitebox.howlook.domain.member.dto.UserPostInfoResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvalReaderDTO {

    private Long NPostId;       //게시글 id

    UserPostInfoResponse userPostInfo;

    private Long LikeCount;     //좋아요개수

    private Long CommentCount;  //댓글개수

    private String Content;     //내용

    private String MainPhotoPath; //사진 경로

    @JsonProperty("regDate")
    private LocalDateTime regDate;

    @JsonProperty("modDate")
    private LocalDateTime modDate;

    private float averageScore;

    @QueryProjection
    public EvalReaderDTO(Evaluation eval) {
        this.NPostId = eval.getNPostId();
        this.userPostInfo = new UserPostInfoResponse(eval.getMember());
        this.LikeCount = eval.getLikeCount();
        this.CommentCount = eval.getCommentCount();
        this.Content = eval.getContent();
        this.MainPhotoPath = eval.getMainPhotoPath();
        this.regDate = eval.getRegDate();
        this.modDate = eval.getModDate();
    }

    public void SetAverage(List<EvalReply> reply)
    {
        this.averageScore = 0;
        List<EvalReply> evalReplies = reply; //evalReplyRepository.findBypid(NPostId);

        for(EvalReply r : evalReplies)
        {

        }
    }
}
