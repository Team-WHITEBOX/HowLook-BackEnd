package org.whitebox.howlook.domain.tournament.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.tournament.dto.THistoryResponse;
import org.whitebox.howlook.domain.tournament.entity.TournamentHistory;
import org.whitebox.howlook.domain.tournament.repository.TournamentRepository;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;

import java.time.LocalDate;

import static org.whitebox.howlook.global.error.ErrorCode.MEMBER_NOT_FOUND;

@Log4j2
@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;
    @Override
    public THistoryResponse getTHistory(LocalDate date) {
        THistoryResponse result = tournamentRepository.findTHistoryResponseByDate(date)
                .orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
        return result;
    }
}
