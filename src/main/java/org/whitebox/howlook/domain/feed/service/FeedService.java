package org.whitebox.howlook.domain.feed.service;

import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;

import java.util.List;

public interface FeedService {
    void registerPOST(FeedRegisterDTO feedRegisterDTO);
    FeedReaderDTO readerPID(Long NPostId);

    List<FeedReaderDTO> readerUID(String UserID);
}
