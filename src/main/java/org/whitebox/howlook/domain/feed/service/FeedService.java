package org.whitebox.howlook.domain.feed.service;

import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;

import java.util.List;

public interface FeedService {
    void register(FeedRegisterDTO feedRegisterDTO);
    FeedReaderDTO readerPID(Long NPostId);

    //FeedReaderDTO readerUID(String UserID);
}
