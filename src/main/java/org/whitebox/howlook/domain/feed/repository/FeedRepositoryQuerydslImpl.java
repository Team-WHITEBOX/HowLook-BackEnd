package org.whitebox.howlook.domain.feed.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.HashtagDTO;
import org.whitebox.howlook.domain.feed.dto.QFeedReaderDTO;
import org.whitebox.howlook.domain.feed.entity.QFeed;

import java.util.List;

import static org.whitebox.howlook.domain.feed.entity.QFeed.feed;
import static org.whitebox.howlook.domain.feed.entity.QHashtag.hashtag;

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

    //hashtagDTO받아서 true 값인 hashtag가 포함된 feed들의 ID를 List로 반환
    @Override
    public List<FeedReaderDTO> findFeedByCategories(HashtagDTO hashtagDTO, Long heightHigh, Long heightLow, Long weightHigh, Long weightLow, char gender, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(feed.member.height.goe(heightLow));
        booleanBuilder.and(feed.member.height.loe(heightHigh));

        booleanBuilder.and(feed.member.weight.goe(weightLow));
        booleanBuilder.and(feed.member.weight.loe(weightHigh));

        booleanBuilder.and(feed.member.gender.eq(gender));

        if(hashtagDTO.getAmekaji())
            booleanBuilder.and(feed.hashtag.amekaji.eq(true));
        else booleanBuilder.and(feed.hashtag.amekaji.eq(false));

        if(hashtagDTO.getCasual())
            booleanBuilder.and(feed.hashtag.casual.eq(true));
        else booleanBuilder.and(feed.hashtag.casual.eq(false));

        if(hashtagDTO.getGuitar())
            booleanBuilder.and(feed.hashtag.guitar.eq(true));
        else booleanBuilder.and(feed.hashtag.guitar.eq(false));

        if(hashtagDTO.getMinimal())
            booleanBuilder.and(feed.hashtag.minimal.eq(true));
        else booleanBuilder.and(feed.hashtag.minimal.eq(false));

        if(hashtagDTO.getSporty())
            booleanBuilder.and(feed.hashtag.sporty.eq(true));
        else booleanBuilder.and(feed.hashtag.sporty.eq(false));

        if(hashtagDTO.getStreet())
            booleanBuilder.and(feed.hashtag.street.eq(true));
        else booleanBuilder.and(feed.hashtag.street.eq(false));

        final List<FeedReaderDTO> feeds = queryFactory
                .select(new QFeedReaderDTO(feed))
                .from(feed)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return feeds;
        //return new PageImpl<>(feedIDs);
    }

    @Override
    public Page<FeedReaderDTO> findNearFeedReaderDTOPage(Pageable pageable,float latitude, float longitude) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(feed.latitude.gt(latitude - 0.05f));
        booleanBuilder.and(feed.latitude.lt(latitude + 0.05f));
        booleanBuilder.and(feed.longitude.gt(longitude - 0.05f));
        booleanBuilder.and(feed.longitude.lt(longitude + 0.05f));

        final List<FeedReaderDTO> feedDtos = queryFactory
                .select(new QFeedReaderDTO(
                        feed
                ))
                .from(feed)
                .where(booleanBuilder)
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
