package org.whitebox.howlook.domain.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.evaluation.entity.CreatorReply;

import java.util.List;
import java.util.Optional;

public interface CreatorReplyRepository   extends JpaRepository<CreatorReply, Long> {


    @Query("select c from CreatorReply c where c.member.memberId = :memberId")
    CreatorReply findBymemberId(String memberId);
    @Query("select c from CreatorReply c where c.evaluation.evalId = :pid AND c.member.memberId = :memberId")
    CreatorReply findMyReplyByPostid(Long pid,String memberId);
    @Query("select c from CreatorReply c where c.evaluation.evalId = :pid")
    Optional<List<CreatorReply>> findBypid(Long pid);
    @Query("select c from CreatorReply c where c.evaluation.evalId = :pid and c.member.memberId = :memberId")
    CreatorReply findBypidAndmemberId(Long pid,String memberId);
}
