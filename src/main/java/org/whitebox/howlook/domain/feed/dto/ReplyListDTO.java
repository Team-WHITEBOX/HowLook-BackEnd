package org.whitebox.howlook.domain.feed.dto;

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
    private String Contents; // 댓글 내용
    private long NPostId; // 게시글 아이디
    private long parentId; // 부모 댓글 아이디
    private long ReplyId; // 댓글 아이디

    //사용자 정보
    private String Nickname; // 사용자 닉네임
    private long profilePhotoId; // 프로필 사진
}
