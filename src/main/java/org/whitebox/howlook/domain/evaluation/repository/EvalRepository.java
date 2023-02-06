package org.whitebox.howlook.domain.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;

import java.util.List;

public interface EvalRepository extends JpaRepository<Evaluation, Long>, EvalSearch,EvalSearchQuerydsl {

    @Query("select e from Evaluation e where e.member.memberId = :memberId")
    List<Evaluation> findBymemberId(String memberId);

    @Query("select e from Evaluation e where e.postId = :pid")
    Evaluation findByPid(Long pid);
}