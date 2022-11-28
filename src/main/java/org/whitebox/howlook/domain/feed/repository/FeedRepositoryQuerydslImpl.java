package org.whitebox.howlook.domain.feed.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.QFeedReaderDTO;

import java.util.List;

import static org.whitebox.howlook.domain.feed.entity.QFeed.feed;

@Log4j2
@RequiredArgsConstructor
public class FeedRepositoryQuerydslImpl implements FeedRepositoryQuerydsl{
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<FeedReaderDTO> findFeedReaderDTOPage(Pageable pageable) {
        final List<FeedReaderDTO> feedDtos = queryFactory
                .select(new QFeedReaderDTO(
                        feed
                ))
                .from(feed)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(feed.NPostId.desc())
                .distinct()
                .fetch();
        final long total = queryFactory
                .selectFrom(feed).fetch().size();
        return new PageImpl<>(feedDtos, pageable, total);
    }
}
