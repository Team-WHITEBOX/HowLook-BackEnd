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


}
