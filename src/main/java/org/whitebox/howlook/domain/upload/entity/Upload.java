package org.whitebox.howlook.domain.upload.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.upload.dto.UploadFileDTO;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "feed")
@DynamicInsert
public class Upload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long PhotoId;       //사진 id

    private String Path;        //사진 경로


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="NPostId")
    private Feed feed;

    @Builder
    public Upload(Feed feed, String path) {
        this.feed = feed;
        this.Path = path;
    }
}
