package org.whitebox.howlook.domain.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.evaluation.entity.CreatorEval;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;

import java.util.List;
import java.util.Optional;

public interface CreatorEvalRepository extends JpaRepository<CreatorEval,Long>, CreatorEvalSearchQuerydsl {
    @Query("select c from CreatorEval c where c.member.memberId = :userId")
    List<CreatorEval> findByUserId(String userId);

    @Query("select c from CreatorEval c where c.evalId = :pid")
    Optional<CreatorEval> findByPid(Long pid);
}