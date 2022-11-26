package org.whitebox.howlook.domain.feed.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.upload.entity.Upload;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "member")
@DynamicInsert
public class Feed extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long NPostId;       //게시글 id

    //private String mid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid")
    private Member member;

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
    
    private Long FeedLocation;    //해당피드 위치정보

    //public Long HashtagId;  //feedServiceImpl에서 해당 값 직접수정하기 위해 public사용

    @OneToMany(mappedBy = "feed")
    @Builder.Default
    private List<Upload> uploads = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "HashtagId")
    private Hashtag h;
    /*
    @OneToMany(mappedBy = "feed")   //해시태그 매핑
    @Builder.Default
    private Set<Hashtag> hashtag = new HashSet<>();
     *///이거 왜 없어도 돌아가는지 확인

    public void setMember(Member member){
        this.member = member;
    }
//    public void setMid(String mid){
//        this.mid=mid;
//    }
}
