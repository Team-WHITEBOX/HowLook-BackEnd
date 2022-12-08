package org.whitebox.howlook.domain.tournament.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
public abstract class FeedToTournaRepositoryImpl implements FeedToTournaRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> FindTop32FeedByDateForTourna()
    {
        List<Map<String, Object>> cnt = new ArrayList<>();
        List<String> str = new ArrayList<>();

        String sql = "SELECT * from Feed, " +
                "(SELECT max(NPostId) AS k from Feed, " +
                "(SELECT max(LikeCount) AS m,member.mid AS n FROM Feed GROUP BY regDate,member.mid ORDER BY max(LikeCount) DESC) AS result " +
                "WHERE DATE_FORMAT(NOW() - INTERVAL 0 DAY,'%Y-%m-%d') = DATE_FORMAT(regdate,'%Y-%m-%d') and LikeCount = result.m and member.mid = result.n GROUP BY member.mid) AS result2 " +
                "WHERE NPostId = result2.k";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        cnt.addAll(rows);

        for(Map<String, Object> map: cnt) {
            for (String path: map.keySet()){
                str.add((String) map.get(path));
            }
        }
        return str;
        //return feedReaderDTOS;
    }

}
