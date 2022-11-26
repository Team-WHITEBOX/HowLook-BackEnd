package org.whitebox.howlook.domain.feed.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.persister.walking.internal.FetchStrategyHelper;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "feed")
@DynamicInsert
public class Hashtag {
    //minimal, casual, street, amekaji, sporty, Guitar
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long HashtagId;     //해시태그 ID

    //해시태그 각 속성들은 True, False로 입력하며 지정하지 않을시 기본적으로 false로 세팅
    @Column(columnDefinition = "BOOLEAN default false")
    private Boolean minimal;

    @Column(columnDefinition = "BOOLEAN default false")
    private Boolean casual;

    @Column(columnDefinition = "BOOLEAN default false")
    private Boolean street;

    @Column(columnDefinition = "BOOLEAN default false")
    private Boolean amekaji;

    @Column(columnDefinition = "BOOLEAN default false")
    private Boolean sporty;

    @Column(columnDefinition = "BOOLEAN default false")
    private Boolean guitar;     //EATER EGG

    @OneToOne
    //@JoinColumn(name = "NpostId")
    private Feed f;

    /*
    @Builder
    public Hashtag(Feed feed) {
        this.feed = feed;
    }
     */
}
