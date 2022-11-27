package org.whitebox.howlook.domain.evaluation.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;
import org.whitebox.howlook.domain.evaluation.entity.QEvaluation;

import java.util.List;

public class EvalSearchImpl extends QuerydslRepositorySupport implements EvalSearch {

    public EvalSearchImpl()
    {
        super(Evaluation.class);
    }

    @Override
    public Page<Evaluation> search1(Pageable pageable)
    {
        QEvaluation evaluation = QEvaluation.evaluation;
        JPQLQuery<Evaluation> query = from(evaluation);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.or(evaluation.Content.contains("3"));
        booleanBuilder.or(evaluation.member.mid.contains("uuuu"));

        query.where(booleanBuilder);
        query.where(evaluation.MainPhotoPath.contains("C:"));

        this.getQuerydsl().applyPagination(pageable, query);

        List<Evaluation> list = query.fetch();
        long count = query.fetchCount();
        return null;
    }
}
