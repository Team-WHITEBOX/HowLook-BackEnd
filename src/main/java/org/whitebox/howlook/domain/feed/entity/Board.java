package org.whitebox.howlook.domain.feed.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno; // 게시글 번호

    private String title; // 제목
    private String content; // 내용
    private String writer; // 작성자

    private String userId; // 작성자
    private String photoCnt; // 게시글의 사진 개수
    private String likeCnt; // 게시글의 좋아요 수
    private String commentCnt; // 게시글 댓글 수
    private String postDate; // 게시글 업로드 날짜
    private String viewCnt; // 게시글의 조회수
    private String mainPhotoPath; // 메인 사진 경로

    @OneToMany(mappedBy = "board")
    @Builder.Default
    private Set<BoardImage> imageSet = new HashSet();
}
