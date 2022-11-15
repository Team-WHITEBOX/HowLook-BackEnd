package org.whitebox.howlook.domain.feed.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.whitebox.howlook.domain.feed.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {


}
