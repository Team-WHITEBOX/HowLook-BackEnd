package org.whitebox.howlook.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
