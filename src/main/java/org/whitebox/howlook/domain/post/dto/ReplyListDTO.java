package org.whitebox.howlook.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyListDTO {
    //댓글 정보
    private String contents; // 댓글 내용
    private long postId; // 게시글 아이디
    private long parentId; // 부모 댓글 아이디
    private long replyId; // 댓글 아이디

    //사용자 정보
    private String nickName; // 사용자 닉네임
    private String profilePhoto; // 프로필 사진
}
