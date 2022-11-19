package org.whitebox.howlook.domain.Reply.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private int CommendId; // 해당 댓글 키

    @NotNull
    private int NPostId; // 게시글 키

    @NotEmpty
    private String UserId; // 사용자 아이디

    @NotEmpty
    private String Contents; // 내용
    private int LikeCount; // 좋아요개수

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime Date; // 댓글 단 날짜

    private int ParentsId; // 부모 댓글 키

    public void setRno(int commendId) {
        CommendId = commendId;
    }
}
