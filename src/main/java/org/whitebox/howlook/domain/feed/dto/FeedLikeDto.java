package org.whitebox.howlook.domain.feed.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class FeedLikeDto {
    private Long NPostId;
    private String username;

    @QueryProjection
    public FeedLikeDto(Long NPostId, String username) {
        this.NPostId = NPostId;
        this.username = username;
    }
}
