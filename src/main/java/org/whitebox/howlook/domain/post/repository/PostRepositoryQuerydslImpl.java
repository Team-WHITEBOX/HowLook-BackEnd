package org.whitebox.howlook.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.dto.HashtagDTO;
import org.whitebox.howlook.domain.post.dto.QPostReaderDTO;
import org.whitebox.howlook.domain.post.entity.QPost;

import java.util.List;

import static org.whitebox.howlook.domain.post.entity.QPost.post;

@Log4j2
@RequiredArgsConstructor
public class PostRepositoryQuerydslImpl implements PostRepositoryQuerydsl {
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<PostReaderDTO> findPostReaderDTOPage(Pageable pageable) {
        final List<PostReaderDTO> postDtos = queryFactory
                .select(new QPostReaderDTO(
                        post
                ))
                .from(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.postId.desc())
                .distinct()
                .fetch();
        final long total = queryFactory
                .selectFrom(post).fetch().size();
        return new PageImpl<>(postDtos, pageable, total);
    }

    //hashtagDTO받아서 true 값인 hashtag가 포함된 post들의 ID를 List로 반환
    @Override
    public List<PostReaderDTO> findpostByCategories(HashtagDTO hashtagDTO, Long heightHigh, Long heightLow, Long weightHigh, Long weightLow, char gender, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(post.member.height.goe(heightLow));
        booleanBuilder.and(post.member.height.loe(heightHigh));

        booleanBuilder.and(post.member.weight.goe(weightLow));
        booleanBuilder.and(post.member.weight.loe(weightHigh));

        booleanBuilder.and(post.member.gender.eq(gender));

        if(hashtagDTO.getAmekaji())
            booleanBuilder.and(post.hashtag.amekaji.eq(true));
        else booleanBuilder.and(post.hashtag.amekaji.eq(false));

        if(hashtagDTO.getCasual())
            booleanBuilder.and(post.hashtag.casual.eq(true));
        else booleanBuilder.and(post.hashtag.casual.eq(false));

        if(hashtagDTO.getGuitar())
            booleanBuilder.and(post.hashtag.guitar.eq(true));
        else booleanBuilder.and(post.hashtag.guitar.eq(false));

        if(hashtagDTO.getMinimal())
            booleanBuilder.and(post.hashtag.minimal.eq(true));
        else booleanBuilder.and(post.hashtag.minimal.eq(false));

        if(hashtagDTO.getSporty())
            booleanBuilder.and(post.hashtag.sporty.eq(true));
        else booleanBuilder.and(post.hashtag.sporty.eq(false));

        if(hashtagDTO.getStreet())
            booleanBuilder.and(post.hashtag.street.eq(true));
        else booleanBuilder.and(post.hashtag.street.eq(false));

        final List<PostReaderDTO> posts = queryFactory
                .select(new QPostReaderDTO(post))
                .from(post)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return posts;
        //return new PageImpl<>(postIDs);
    }

    @Override
    public Page<PostReaderDTO> findNearPostReaderDTOPage(Pageable pageable, float latitude, float longitude) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(post.latitude.gt(latitude - 0.05f));
        booleanBuilder.and(post.latitude.lt(latitude + 0.05f));
        booleanBuilder.and(post.longitude.gt(longitude - 0.05f));
        booleanBuilder.and(post.longitude.lt(longitude + 0.05f));

        final List<PostReaderDTO> postDtos = queryFactory
                .select(new QPostReaderDTO(
                        post
                ))
                .from(post)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.postId.desc())
                .distinct()
                .fetch();
        final long total = queryFactory
                .selectFrom(post).fetch().size();
        return new PageImpl<>(postDtos, pageable, total);
    }
}
