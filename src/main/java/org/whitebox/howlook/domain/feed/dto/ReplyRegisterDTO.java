package org.whitebox.howlook.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRegisterDTO {
    private long NPostId; // 댓글을 올린 게시글의 아이디

    private String Contents; // 댓글 내용

    private long parentId; // 부모 댓글 ID
}
