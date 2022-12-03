package org.whitebox.howlook.domain.feed.service;

import org.springframework.data.domain.Page;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;

import java.util.List;

public interface FeedService {
    void registerPOST(FeedRegisterDTO feedRegisterDTO);
    FeedReaderDTO readerPID(Long NPostId);

    List<FeedReaderDTO> readerUID(String UserID);

    Page<FeedReaderDTO> getFeedPage(int size,int page);

    public void scrapFeed(Long npost_id);

    public void unScrapFeed(Long npost_id);

    public void likeFeed(Long NPostId);

    public void unlikeFeed(Long NPostId);
}
