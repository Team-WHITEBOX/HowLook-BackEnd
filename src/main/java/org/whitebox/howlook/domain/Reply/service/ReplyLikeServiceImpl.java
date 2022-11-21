package org.whitebox.howlook.domain.Reply.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.Reply.entity.CommentLike;
import org.whitebox.howlook.domain.Reply.entity.Reply;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyLikeServiceImpl {
    private final ReplyLikeRepository replyLikeRepository;

    @Transactional
    public void deleteAll(List<Reply> replies) {
        final List<ReplyLike> re
    }
}
