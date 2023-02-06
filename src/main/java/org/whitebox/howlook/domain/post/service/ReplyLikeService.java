package org.whitebox.howlook.domain.post.service;

import org.whitebox.howlook.domain.post.entity.Reply;
import org.whitebox.howlook.domain.post.repository.ReplyLikeRepository;

import java.util.List;


public interface ReplyLikeService {
    void deleteAll(List<Reply> replies);
}
