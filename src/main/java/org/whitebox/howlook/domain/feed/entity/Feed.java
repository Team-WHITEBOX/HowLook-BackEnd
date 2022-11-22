package org.whitebox.howlook.domain.feed.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.whitebox.howlook.domain.upload.entity.Upload;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    // npost_id를 통해 사진을 가져오는 get Method가 구현되어서 엔티티 구조 변경
    private String MainPhotoPath; //사진 경로
    
    private Long FeedLocation;    //해당피드 위치정보

    @OneToMany(mappedBy = "feed")
    @Builder.Default
    private List<Upload> uploads = new ArrayList<>();
}
