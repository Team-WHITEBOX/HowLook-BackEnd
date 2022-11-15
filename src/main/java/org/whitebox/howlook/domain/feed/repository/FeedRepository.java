package org.whitebox.howlook.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.feed.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
