package org.whitebox.howlook.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private long ReplyId;

    private long NPostId;

    private String Contents;

    private long parentId;
}
