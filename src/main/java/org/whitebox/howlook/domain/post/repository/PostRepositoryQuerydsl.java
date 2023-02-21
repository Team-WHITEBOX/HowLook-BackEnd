package org.whitebox.howlook.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.dto.SearchCategoryDTO;

import java.util.List;

public interface PostRepositoryQuerydsl {
    public Page<PostReaderDTO> findPostReaderDTOPage(Pageable pageable);

    public List<PostReaderDTO> findPostByCategories(SearchCategoryDTO searchCategoryDTO, Pageable pageable);

    public Page<PostReaderDTO> findNearPostReaderDTOPage(Pageable pageable, float latitude, float longitude);
}
