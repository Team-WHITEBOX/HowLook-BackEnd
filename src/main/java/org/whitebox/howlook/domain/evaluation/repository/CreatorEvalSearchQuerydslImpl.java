package org.whitebox.howlook.domain.evaluation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.evaluation.dto.CreatorEvalReadDTO;
import org.whitebox.howlook.domain.evaluation.dto.EvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.dto.QCreatorEvalReadDTO;
import org.whitebox.howlook.domain.evaluation.dto.QEvalReaderDTO;
import org.whitebox.howlook.domain.evaluation.entity.CreatorEval;
import org.whitebox.howlook.domain.evaluation.entity.QCreatorEval;

import java.util.List;

import static org.whitebox.howlook.domain.evaluation.entity.QCreatorEval.creatorEval;
import static org.whitebox.howlook.domain.evaluation.entity.QEvaluation.evaluation;

@Log4j2
@RequiredArgsConstructor
public class CreatorEvalSearchQuerydslImpl implements CreatorEvalSearchQuerydsl{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CreatorEvalReadDTO> findCreatorEvalReadDTOPage(Pageable pageable){

        final List<CreatorEvalReadDTO> creatorEvalReadDTOS = queryFactory
                .select(new QCreatorEvalReadDTO(
                        creatorEval
                ))
                .from(creatorEval)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(creatorEval.evalId.desc())
                .distinct()
                .fetch();


        final long total = queryFactory
                .selectFrom(evaluation).fetch().size();

        return new PageImpl<>(creatorEvalReadDTOS, pageable, total);
    }
}
