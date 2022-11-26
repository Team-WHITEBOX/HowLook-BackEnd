package org.whitebox.howlook.domain.feed.service;

import org.whitebox.howlook.domain.feed.dto.HashtagDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;

public interface HashtagService {
    void registerHashtag(HashtagDTO hashtagDTO);
}
