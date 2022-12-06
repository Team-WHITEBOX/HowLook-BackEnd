package org.whitebox.howlook.domain.feed.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "f")
@DynamicInsert
public class Hashtag {
    //minimal, casual, street, amekaji, sporty, Guitar
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long HashtagId;     //해시태그 ID

    //해시태그 각 속성들은 True, False로 입력하며 지정하지 않을시 기본적으로 false로 세팅
    @Column(name = "minimal")
    @ColumnDefault("false")
    private Boolean minimal;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean casual;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean street;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean amekaji;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean sporty;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean guitar;     //EATER EGG

    @OneToOne
    private Feed f;

    public void setFeed(Feed feed){
        this.f = feed;
    }
}
