package org.whitebox.howlook.domain.evaluation.dto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;

public interface EvalRepository extends JpaRepository<Evaluation, Long> {

}