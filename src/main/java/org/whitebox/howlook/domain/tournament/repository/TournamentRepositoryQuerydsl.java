package org.whitebox.howlook.domain.tournament.repository;

import org.whitebox.howlook.domain.tournament.dto.THistoryResponse;
import org.whitebox.howlook.domain.tournament.entity.TournamentHistory;

import java.time.LocalDate;
import java.util.Optional;

public interface TournamentRepositoryQuerydsl {
    Optional<THistoryResponse> findTHistoryResponseByDate(LocalDate date);
}
