package org.whitebox.howlook.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.feed.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
