package org.whitebox.howlook.domain.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;

import java.util.List;

public interface EvalReplyRepository  extends JpaRepository<EvalReply, Long> {

    @Query("select e from EvalReply e where e.member.mid = :mid")
    EvalReply findByMid(String mid);
    @Query("select e from EvalReply e where e.evaluation.NPostId = :pid AND e.member.mid = :mid")
    EvalReply findMyReplyByPostid(Long pid,String mid);
    @Query("select e from EvalReply e where e.evaluation.NPostId = :pid")
    List<EvalReply> findBypid(Long pid);
    @Query("select e from EvalReply e where e.evaluation.NPostId = :pid and e.member.mid = :mid")
    EvalReply findBypidAndmid(Long pid,String mid);

}
