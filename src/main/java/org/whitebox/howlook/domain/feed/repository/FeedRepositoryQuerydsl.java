package org.whitebox.howlook.domain.feed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.HashtagDTO;

import java.util.List;

public interface FeedRepositoryQuerydsl {
    public Page<FeedReaderDTO> findFeedReaderDTOPage(Pageable pageable);

    public List<FeedReaderDTO> findFeedByCategories(HashtagDTO hashtagDTO, Long heightHigh, Long heightLow, Long weightHigh, Long weightLow, char gender, Pageable pageable);
}
