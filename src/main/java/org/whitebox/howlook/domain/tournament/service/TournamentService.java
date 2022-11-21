package org.whitebox.howlook.domain.tournament.service;


import org.whitebox.howlook.domain.tournament.dto.THistoryResponse;
import org.whitebox.howlook.domain.tournament.entity.TournamentHistory;

import java.time.LocalDate;

public interface TournamentService {
    THistoryResponse getTHistory(LocalDate date);
}
