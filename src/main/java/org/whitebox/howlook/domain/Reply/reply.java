package org.whitebox.howlook.domain.Reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int CommendId; // 해당 댓글 키

    private int NPostId; // 게시글 키
    private String UserId; // 사용자 아이디
    private String Contents; // 내용
    private int LikeCount; // 좋아요개수
    private LocalDateTime Date; // 댓글 단 날짜
    private int ParentsId; // 부모 댓글 키

    public void changeContents(String text) {
        this.Contents = text;
    }
}