package org.whitebox.howlook.domain.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class PostLikeDTO {

    @NotNull(message = "포스트에 좋아요표시 요청을 위해서 PostId가 필요하오이다.")
    @Positive
    private Long postId;
    
    @NotEmpty(message = "좋아요 표시한 사람의 이름이 필요하외다.")
    private String username;

    @QueryProjection
    public PostLikeDTO(Long postId, String username) {
        this.postId = postId;
        this.username = username;
    }
}
