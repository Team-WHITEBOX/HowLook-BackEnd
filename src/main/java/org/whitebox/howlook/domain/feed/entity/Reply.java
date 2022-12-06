package org.whitebox.howlook.domain.feed.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.whitebox.howlook.domain.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "Reply")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicInsert
public class Reply extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ReplyId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String nickName;

    private Long profilePhotoId;
    private String contents;

//    public Long photoId; // 프로필ID
    @ColumnDefault("0")
    private Long parentsId;

    private Long LikeCount;

    public void changeText(String text) {
        this.contents = text;
    }

    public void Up_LikeCount() {
        this.LikeCount++;
    }

    public void Down_LikeCount() {
        this.LikeCount--;
    }
}