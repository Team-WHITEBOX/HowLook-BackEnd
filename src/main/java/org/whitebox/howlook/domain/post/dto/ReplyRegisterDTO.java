package org.whitebox.howlook.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRegisterDTO {
    @Positive
    private long postId; // 댓글을 올린 게시글의 아이디

    @NotBlank(message = "댓글을 입력해주세요")
    @Size(min = 1, max = 1000, message = "댓글은 1문자 이상 1000문자 이하여야 합니다.")
    private String content; // 댓글 내용

    @Positive
    @NotNull
    private long parentId; // 부모 댓글 ID
}
