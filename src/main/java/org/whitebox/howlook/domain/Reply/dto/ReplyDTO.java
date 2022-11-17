package org.whitebox.howlook.domain.Reply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private int CommendId; // 해당 댓글 키
    private int NPostId; // 게시글 키
    private String UserId; // 사용자 아이디
    private String Contents; // 내용
    private int LikeCount; // 좋아요개수
    private LocalDateTime Date; // 댓글 단 날짜
    private int ParentsId; // 부모 댓글 키
}
