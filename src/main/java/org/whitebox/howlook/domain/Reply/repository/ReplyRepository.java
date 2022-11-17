package org.whitebox.howlook.domain.Reply.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.Reply.reply;

public interface ReplyRepository extends JpaRepository<reply,Integer> {
    @Query("select r from reply r")
    Page<reply> listofBoard(int bno, Pageable pageable);
}