package org.whitebox.howlook.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimplePostDTO {
    private Long postId;       //게시글 id
    private String mainPhotoPath; //사진 경로
}
