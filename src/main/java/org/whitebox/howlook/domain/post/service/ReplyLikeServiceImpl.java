package org.whitebox.howlook.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.post.entity.Reply;
import org.whitebox.howlook.domain.post.entity.ReplyLike;
import org.whitebox.howlook.domain.post.repository.ReplyLikeRepository;

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
