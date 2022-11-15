package org.whitebox.howlook.domain.feed.service;

import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;

public interface FeedService {
    void register(FeedRegisterDTO feedRegisterDTO);

    FeedReaderDTO reader(Long NPostId);
}
