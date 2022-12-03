package org.whitebox.howlook.domain.feed.service;

import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.FeedLike;

import java.util.List;

public interface FeedLikeService {
    void deleteAll(Feed feed);
    List<FeedLike> getAllWithMember(Long NPostId);
}
