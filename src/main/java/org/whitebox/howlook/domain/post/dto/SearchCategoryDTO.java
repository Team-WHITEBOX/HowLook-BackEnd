package org.whitebox.howlook.domain.post.dto;

import lombok.Data;

@Data
public class SearchCategoryDTO {
    private HashtagDTO hashtagDTO;

    private Long heightHigh;
    private Long heightLow;

    private Long weightHigh;
    private Long weightLow;

    private char gender;

    private int page;
    private int size;
}
