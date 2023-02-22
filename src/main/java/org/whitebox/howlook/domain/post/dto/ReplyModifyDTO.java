package org.whitebox.howlook.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReplyModifyDTO {
    private String Content; // 바꿀 댓글 내용.
}