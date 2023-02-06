package org.whitebox.howlook.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.dto.HashtagDTO;

import java.util.List;

public interface PostRepositoryQuerydsl {
    public Page<PostReaderDTO> findPostReaderDTOPage(Pageable pageable);
    public List<PostReaderDTO> findpostByCategories(HashtagDTO hashtagDTO, Long heightHigh, Long heightLow, Long weightHigh, Long weightLow, char gender, Pageable pageable);
    public Page<PostReaderDTO> findNearPostReaderDTOPage(Pageable pageable, float latitude, float longitude);
}
