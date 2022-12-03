package org.whitebox.howlook.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.FeedLike;
import org.whitebox.howlook.domain.feed.repository.FeedLikeRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedLikeServiceImpl {

    private final FeedLikeRepository feedLikeRepository;

    @Transactional
    public void deleteAll(Feed feed) {
        final List<FeedLike> feedLikes = feedLikeRepository.findAllByFeed(feed);
        feedLikeRepository.deleteAllInBatch(feedLikes);
    }

    public List<FeedLike> getAllWithMember(Long NPostId) {
        return feedLikeRepository.findAllWithMemberByNPostId(NPostId);
    }
}
