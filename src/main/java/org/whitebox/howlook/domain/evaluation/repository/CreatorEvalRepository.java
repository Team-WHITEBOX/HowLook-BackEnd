package org.whitebox.howlook.domain.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.evaluation.entity.CreatorEval;

import java.util.List;

public interface CreatorEvalRepository extends JpaRepository<CreatorEval,Long>, CreatorEvalSearchQuerydsl {
    @Query("select c from CreatorEval c where c.member = :userId")
    List<CreatorEval> findByUserId(String userId);
}