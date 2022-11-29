package org.whitebox.howlook.domain.feed.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;

public interface FeedRepositoryQuerydsl {
    public Page<FeedReaderDTO> findFeedReaderDTOPage(Pageable pageable);
    public Page<FeedReaderDTO> findNearFeedReaderDTOPage(Pageable pageable,float latitude, float longitude);
}
