package org.whitebox.howlook.domain.evaluation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;

public interface EvalSearch {
    public Page<Evaluation> search1(Pageable pageable);
}
