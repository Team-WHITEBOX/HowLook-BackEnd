package org.whitebox.howlook.domain.feed.service;

import org.springframework.data.domain.Page;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;

import java.util.List;

public interface FeedService {
    void registerPOST(FeedRegisterDTO feedRegisterDTO);
    FeedReaderDTO readerPID(Long NPostId);

    Page<FeedReaderDTO> readP

    List<FeedReaderDTO> readerUID(String UserID);
}
