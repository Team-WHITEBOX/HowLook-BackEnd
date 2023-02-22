package org.whitebox.howlook.domain.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.whitebox.howlook.domain.post.entity.Reply;

@Getter
@Setter
@NoArgsConstructor
public class ReplyReadDTO {
    // 댓글 정보
    private long replyId;
    private long postId;
    private String content;
    private long parentId;
    private long likeCount;
    // 유저 정보
    private String nickName; // 유저의 별명
    private String profilePhoto; // 프로필
    private Boolean likeCheck; // 댓글에 대한 좋아요 체크여부

    @QueryProjection
    public ReplyReadDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.postId = reply.getPost().getPostId();
        this.content = reply.getContent();
        this.parentId = reply.getParentId();
        this.likeCount = reply.getLikeCount();
        this.nickName = reply.getMember().getNickName();
        this.profilePhoto = reply.getMember().getProfilePhoto();
        this.likeCheck = false;
    }
}
