package org.whitebox.howlook.domain.tournament.repository;

import org.whitebox.howlook.domain.tournament.dto.EHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.THistoryList;

import java.time.LocalDate;
import java.util.Optional;

public interface TournamentRepositoryQuerydsl {
    Optional<THistoryList> findTHistoryListByDate(LocalDate date);
    Optional<EHistoryResponse> findEHistoryResponseByDate(LocalDate date);
}
