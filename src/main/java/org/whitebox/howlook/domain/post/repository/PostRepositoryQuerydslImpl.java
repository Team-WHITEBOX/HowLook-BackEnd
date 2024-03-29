package org.whitebox.howlook.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.dto.QPostReaderDTO;
import org.whitebox.howlook.domain.post.dto.SearchCategoryDTO;

import java.util.List;

import static org.whitebox.howlook.domain.post.entity.QPost.post;

@Log4j2
@RequiredArgsConstructor
public class PostRepositoryQuerydslImpl implements PostRepositoryQuerydsl {
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<PostReaderDTO> findPostReaderDTOPage(Pageable pageable) {
        final List<PostReaderDTO> postDTOs = queryFactory
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
        return new PageImpl<>(postDTOs, pageable, total);
    }

    //hashtagDTO받아서 true 값인 hashtag가 포함된 post들의 ID를 List로 반환
    @Override
    public Page<PostReaderDTO> findPostByCategories(SearchCategoryDTO searchCategoryDTO, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(post.member.height.goe(searchCategoryDTO.getHeightLow()));
        booleanBuilder.and(post.member.height.loe(searchCategoryDTO.getHeightHigh()));

        booleanBuilder.and(post.member.weight.goe(searchCategoryDTO.getWeightLow()));
        booleanBuilder.and(post.member.weight.loe(searchCategoryDTO.getWeightHigh()));

        if(searchCategoryDTO.getGender() != 'A') {
            booleanBuilder.and(post.member.gender.eq(searchCategoryDTO.getGender()));
        }

        if(searchCategoryDTO.getHashtagDTOAmekaji())
            booleanBuilder.and(post.hashtag.amekaji.eq(true));
        else booleanBuilder.and(post.hashtag.amekaji.eq(false));

        if(searchCategoryDTO.getHashtagDTOCasual())
            booleanBuilder.and(post.hashtag.casual.eq(true));
        else booleanBuilder.and(post.hashtag.casual.eq(false));

        if(searchCategoryDTO.getHashtagDTOGuitar())
            booleanBuilder.and(post.hashtag.guitar.eq(true));
        else booleanBuilder.and(post.hashtag.guitar.eq(false));

        if(searchCategoryDTO.getHashtagDTOMinimal())
            booleanBuilder.and(post.hashtag.minimal.eq(true));
        else booleanBuilder.and(post.hashtag.minimal.eq(false));

        if(searchCategoryDTO.getHashtagDTOSporty())
            booleanBuilder.and(post.hashtag.sporty.eq(true));
        else booleanBuilder.and(post.hashtag.sporty.eq(false));

        if(searchCategoryDTO.getHashtagDTOStreet())
            booleanBuilder.and(post.hashtag.street.eq(true));
        else booleanBuilder.and(post.hashtag.street.eq(false));

        final List<PostReaderDTO> posts = queryFactory
                .select(new QPostReaderDTO(post))
                .from(post)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        final long total = queryFactory
                .select(new QPostReaderDTO(post))
                .from(post)
                .where(booleanBuilder)
                .fetch()
                .size();
        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<PostReaderDTO> findNearPostReaderDTOPage(Pageable pageable, float latitude, float longitude) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(post.latitude.gt(latitude - 0.05f));
        booleanBuilder.and(post.latitude.lt(latitude + 0.05f));
        booleanBuilder.and(post.longitude.gt(longitude - 0.05f));
        booleanBuilder.and(post.longitude.lt(longitude + 0.05f));

        QueryResults<PostReaderDTO> result = queryFactory
                .select(new QPostReaderDTO(
                        post
                ))
                .from(post)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.postId.desc())
                .distinct()
                .fetchResults();
        final List<PostReaderDTO> postDtos = result.getResults();

        final long total = result.getTotal();

        return new PageImpl<>(postDtos, pageable, total);
    }

    @Override
    public Page<PostReaderDTO> findTemperaturePostReaderDTOPage(Pageable pageable, Long temperature){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(post.temperature.gt(temperature - 2L));
        booleanBuilder.and(post.temperature.lt(temperature + 2L));

        QueryResults<PostReaderDTO> result = queryFactory
                .select(new QPostReaderDTO(
                        post
                ))
                .from(post)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.regDate.desc())
                .distinct()
                .fetchResults();


        final List<PostReaderDTO> postDtos = result.getResults();

        final long total = result.getTotal();

        return new PageImpl<>(postDtos, pageable, total);
    }

}
