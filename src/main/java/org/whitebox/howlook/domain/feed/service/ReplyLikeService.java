package org.whitebox.howlook.domain.feed.service;

import org.whitebox.howlook.domain.feed.entity.Reply;
import org.whitebox.howlook.domain.feed.repository.ReplyLikeRepository;

import java.util.List;


public interface ReplyLikeService {
    void deleteAll(List<Reply> replies);
}
