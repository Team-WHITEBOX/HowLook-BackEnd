package org.whitebox.howlook.domain.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;

import java.util.List;

public interface EvalRepository extends JpaRepository<Evaluation, Long> {

    @Query("select e from Evaluation e where e.member.mid = :mid")
    List<Evaluation> findByMid(String mid);

    @Query("select e from Evaluation e where e.NPostId = :pid")
    Evaluation findByPid(Long pid);
}