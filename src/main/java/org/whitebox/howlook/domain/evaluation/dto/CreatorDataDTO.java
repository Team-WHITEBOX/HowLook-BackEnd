package org.whitebox.howlook.domain.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CreatorDataDTO {
    private List<CreatorReviewDTO> creatorReviewDTO;

    public CreatorDataDTO()
    {

    }
}
