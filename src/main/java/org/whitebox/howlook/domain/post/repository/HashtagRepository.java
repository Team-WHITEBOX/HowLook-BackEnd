package org.whitebox.howlook.domain.post.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.post.entity.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
