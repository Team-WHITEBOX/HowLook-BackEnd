package org.whitebox.howlook.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.entity.Reply;
import org.whitebox.howlook.domain.feed.entity.ReplyLike;
import org.whitebox.howlook.domain.feed.repository.ReplyLikeRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyLikeServiceImpl {
    private final ReplyLikeRepository replyLikeRepository;

    @Transactional
    public void deleteAll(List<Reply> replies) {
        final List<ReplyLike> replyLikes = replyLikeRepository.findAllByReplyIn(replies);
        replyLikeRepository.deleteAllInBatch(replyLikes);
    }
}
