package org.whitebox.howlook.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.post.dto.QReplyReadDTO;
import org.whitebox.howlook.domain.post.dto.ReplyReadDTO;
import org.whitebox.howlook.domain.post.entity.Post;

import java.util.List;

import static org.whitebox.howlook.domain.post.entity.QReply.reply;

@RequiredArgsConstructor
public class ReplyRepositoryQuerydslImp implements ReplyRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReplyReadDTO> findAllByPost(Pageable pageable, Post post) {
        List<ReplyReadDTO> readDTOS = queryFactory
                .select(new QReplyReadDTO(reply))
                .from(reply)
                .where(reply.post.eq(post))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(reply.replyId.desc())
                .fetch();

        final long total = queryFactory
                .selectFrom(reply).where(reply.post.eq(post)).fetch().size();

        return new PageImpl<>(readDTOS, pageable, total);
    }

}
