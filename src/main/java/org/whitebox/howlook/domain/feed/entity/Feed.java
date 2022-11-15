package org.whitebox.howlook.domain.feed.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicInsert
public class Feed extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long NPostId;       //게시글 id

    @Column(columnDefinition = "INT default 0")
    private Long PhotoCnt;      //업로드한 사진 개수

    @Column(columnDefinition = "INT default 0")
    private Long LikeCount;     //좋아요개수

    @Column(columnDefinition = "INT default 0")
    private Long CommentCount;  //댓글개수

    @Column(columnDefinition = "INT default 0")
    private Long ViewCnt;       //조회수

    private String Content;     //내용

    private String MainPhotoPath; //사진 경로
}
