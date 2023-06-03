package org.whitebox.howlook.domain.post.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.upload.entity.Upload;
import springfox.documentation.swagger2.mappers.LicenseMapper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member", "hashtag"})
@DynamicInsert
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;  //게시글 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(columnDefinition = "INT default 0")
    private Long photoCount;      //업로드한 사진 개수

    @Column(columnDefinition = "INT default 0")
    private Long likeCount;     //좋아요개수

    @Column(columnDefinition = "INT default 0")
    private Long postReplyCount;  //댓글개수

    @Column(columnDefinition = "INT default 0")
    private Long viewCount;       //조회수

    private String Content;     //내용

    // postId를 통해 사진을 가져오는 get Method가 구현되어서 엔티티 구조 변경
    private String mainPhotoPath; //사진 경로

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<Upload> uploads = new ArrayList<>();

    private float latitude; // 위도
    private float longitude; // 경도

    private Long temperature; // 온도
    private Long weather; // 날씨

    @OneToOne
    private Hashtag hashtag;

    public void setHashtag(Hashtag hashtag){
        this.hashtag = hashtag;
    }

    public void setMember(Member member){
        this.member = member;
    }

    public void UplikeCount() {
        this.likeCount++;
    }

    public void DownLikeCount() {
        this.likeCount--;
    }

    public void UpCommentCount() {this.postReplyCount++;};

    public void DownCommentCount() {this.postReplyCount--;};
}
