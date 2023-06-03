package org.whitebox.howlook.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReplyPageDTO {
    // 필요 기능 = 현재 페이지, 총 페이지, 페이지 안에 있는 댓글

    private List<ReplyReadDTO> replies; // 페이지 안에 있는 댓글

    private int totalPages; // 총 페이지

    private int nowPage; // 현재

    public ReplyPageDTO(Page<ReplyReadDTO> page){
        this.replies = page.getContent();
        this.totalPages = page.getTotalPages() - 1;
        this.nowPage = page.getNumber();
    }
}
