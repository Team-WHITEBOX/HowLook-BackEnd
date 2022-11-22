package org.whitebox.howlook.domain.feed.service;

import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;

import java.util.List;

public interface FeedService {
    void register(FeedRegisterDTO feedRegisterDTO);
    FeedReaderDTO readerPID(Long NPostId);

    List<FeedReaderDTO> readerUID(String UserID);
}
