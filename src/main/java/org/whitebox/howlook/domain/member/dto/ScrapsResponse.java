package org.whitebox.howlook.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import org.whitebox.howlook.domain.post.dto.SimplePostDTO;

import java.util.List;

@Getter
@Builder
public class ScrapsResponse {
    private List<SimplePostDTO> scraps;
}
