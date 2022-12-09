package org.whitebox.howlook.domain.feed.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.feed.entity.Reply;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyReadDTO {
    // 댓글 정보
    private long NpostId;
    private String Contents;
    private long parentId;

    // 유저 정보
    private String nickName;
    private String profilePhoto;

    @QueryProjection
    public ReplyReadDTO(Reply reply) {
        this.NpostId = reply.getFeed().getNPostId();
        this.Contents = reply.getContents();
        this.parentId = reply.getParentsId();
        this.nickName = reply.getMember().getNickName();
        this.profilePhoto = reply.getMember().getProfilePhoto();
    }
}
