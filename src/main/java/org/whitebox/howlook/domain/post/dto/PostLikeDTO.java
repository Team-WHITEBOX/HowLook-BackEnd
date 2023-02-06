package org.whitebox.howlook.domain.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class PostLikeDTO {
    private Long postId;
    private String username;

    @QueryProjection
    public PostLikeDTO(Long postId, String username) {
        this.postId = postId;
        this.username = username;
    }
}
