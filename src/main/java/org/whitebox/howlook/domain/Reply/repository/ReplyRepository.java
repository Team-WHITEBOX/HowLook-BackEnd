package org.whitebox.howlook.domain.Reply.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.Reply.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply,Integer> {
    @Query("select r from Reply r")
    Page<Reply> listofBoard(int bno, Pageable pageable);
}