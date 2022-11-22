package org.whitebox.howlook.domain.evaluation.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.web.multipart.MultipartFile;
import org.whitebox.howlook.domain.feed.entity.BaseEntity;
import org.whitebox.howlook.domain.upload.entity.Upload;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicInsert
@Setter

public class Evaluation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long NPostId;       //게시글 id

    @Column(columnDefinition = "INT default 0")
    private Long LikeCount;     //좋아요개수

    @Column(columnDefinition = "INT default 0")
    private Long CommentCount;  //댓글개수

    private String Content;     //내용

    private String MainPhotoPath; //사진 경로
}
