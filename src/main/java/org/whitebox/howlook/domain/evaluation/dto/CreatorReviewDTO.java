package org.whitebox.howlook.domain.evaluation.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreatorReviewDTO {

    // 크리에이터 닉네임
    private String nickname;

    // 크리에이터 프로필
    private String mainPhotoPath;

    // 매긴 점수
    private Long score;

    // 남긴 리뷰
    private String review;

    //게시글 id
    private Long postId;

    public CreatorReviewDTO() {
    }
}
