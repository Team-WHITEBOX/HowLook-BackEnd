package org.whitebox.howlook.domain.feed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.feed.entity.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
