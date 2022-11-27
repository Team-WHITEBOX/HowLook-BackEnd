package org.whitebox.howlook.domain.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.board.entity.Board;
import org.whitebox.howlook.domain.evaluation.entity.Evaluation;

public abstract class BoardSearch {
    public abstract Page<Evaluation> search1(Pageable pageable);
}
