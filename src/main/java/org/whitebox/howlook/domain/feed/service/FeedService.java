package org.whitebox.howlook.domain.feed.service;

import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.dto.HashtagDTO;

import java.util.List;

public interface FeedService {
    void registerPOST(FeedRegisterDTO feedRegisterDTO);
    FeedReaderDTO readerPID(Long NPostId);

    List<FeedReaderDTO> readerUID(String UserID);

    public void scrapFeed(Long npost_id);

    public void unScrapFeed(Long npost_id);

    public void searchFeedByHashtag(HashtagDTO hashtagDTO);
}
