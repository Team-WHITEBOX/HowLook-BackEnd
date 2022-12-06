package org.whitebox.howlook.domain.evaluation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.dto.QEvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.service.EvalService;

import java.util.List;

import static org.whitebox.howlook.domain.evaluation.entity.QEvaluation.evaluation;

@Log4j2
@RequiredArgsConstructor
public class EvalSearchQuerydslImpl implements EvalSearchQuerydsl{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<EvalReaderDTO> findEvalReaderDTOPage(Pageable pageable){

        final List<EvalReaderDTO> evalDTOs = queryFactory
                .select(new QEvalReaderDTO(
                        evaluation
                ))
                .from(evaluation)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(evaluation.NPostId.desc())
                .distinct()
                .fetch();

        final long total = queryFactory
                .selectFrom(evaluation).fetch().size();

        return new PageImpl<>(evalDTOs, pageable, total);
    }
}
