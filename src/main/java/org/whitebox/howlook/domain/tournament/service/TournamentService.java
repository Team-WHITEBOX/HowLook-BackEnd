package org.whitebox.howlook.domain.tournament.service;


import org.whitebox.howlook.domain.tournament.dto.EHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.THistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.TournamentPostDTO;
import org.whitebox.howlook.domain.tournament.entity.TournamentHistory;

import java.time.LocalDate;
import java.util.List;

public interface TournamentService {
    THistoryResponse getTHistory(LocalDate date);
    EHistoryResponse getEHistory(LocalDate date);

    List<TournamentPostDTO> getPosts(LocalDate date);
}
