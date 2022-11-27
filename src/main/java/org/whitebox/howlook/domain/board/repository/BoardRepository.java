package org.whitebox.howlook.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
