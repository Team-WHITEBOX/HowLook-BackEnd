package org.whitebox.howlook.domain.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.tournament.entity.TournamentHistory;

public interface HistoryRepository extends JpaRepository<TournamentHistory,Long> {
}
