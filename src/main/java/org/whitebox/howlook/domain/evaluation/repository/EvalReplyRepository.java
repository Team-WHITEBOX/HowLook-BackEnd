package org.whitebox.howlook.domain.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.evaluation.entity.EvalReply;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;

import java.util.List;

public interface EvalReplyRepository  extends JpaRepository<EvalReply, Long> {

    @Query("select e from EvalReply e where e.member.memberId = :memberId")
    EvalReply findBymemberId(String memberId);
    @Query("select e from EvalReply e where e.evaluation.postId = :pid AND e.member.memberId = :memberId")
    EvalReply findMyReplyByPostid(Long pid,String memberId);
    @Query("select e from EvalReply e where e.evaluation.postId = :pid")
    List<EvalReply> findBypid(Long pid);
    @Query("select e from EvalReply e where e.evaluation.postId = :pid and e.member.memberId = :memberId")
    EvalReply findBypidAndmemberId(Long pid,String memberId);

}
