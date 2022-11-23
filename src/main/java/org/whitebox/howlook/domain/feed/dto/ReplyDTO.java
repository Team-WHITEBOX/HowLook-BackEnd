package org.whitebox.howlook.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private long CommentId;

    private long NPostId;

    private String Contents;
}
