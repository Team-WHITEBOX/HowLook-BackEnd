package org.whitebox.howlook.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostPageDTO {
    private List<PostReaderDTO> content;
    private int totalPages;
    private int number;

    public PostPageDTO(Page<PostReaderDTO> page){
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
    }
}
