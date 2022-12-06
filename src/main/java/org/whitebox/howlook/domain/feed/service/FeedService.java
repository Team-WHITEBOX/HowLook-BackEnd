package org.whitebox.howlook.domain.feed.service;

import org.springframework.data.domain.Page;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.dto.HashtagDTO;

import java.util.List;
import java.util.Optional;

public interface FeedService {
    void registerPOST(FeedRegisterDTO feedRegisterDTO);
    FeedReaderDTO readerPID(Long NPostId);

    List<FeedReaderDTO> readerUID(String UserID);

    Page<FeedReaderDTO> getFeedPage(int size,int page);

    Page<FeedReaderDTO> getNearFeedPage(int size,int page,float latitude, float longitude);

    public void scrapFeed(Long npost_id);

    public void unScrapFeed(Long npost_id);

    public List<FeedReaderDTO> searchFeedByHashtag(HashtagDTO hashtagDTO, Long heightHigh, Long heightLow, Long weightHigh, Long weightLow, char gender, int page, int size);

    public void deleteFeed(Long npost_id);
}
