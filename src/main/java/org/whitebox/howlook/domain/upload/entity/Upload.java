package org.whitebox.howlook.domain.upload.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.whitebox.howlook.domain.post.entity.Post;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "post")
@DynamicInsert
public class Upload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uploadId;       //사진 id

    private String path;        //사진 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="postId")
    private Post post;

    @Builder
    public Upload(Post post, String path) {
        this.post = post;
        this.path = path;
    }

}
