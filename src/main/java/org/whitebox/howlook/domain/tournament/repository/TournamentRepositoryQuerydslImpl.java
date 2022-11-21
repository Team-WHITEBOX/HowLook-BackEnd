package org.whitebox.howlook.domain.tournament.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.whitebox.howlook.domain.tournament.dto.EHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.QEHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.QTHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.THistoryResponse;
import org.whitebox.howlook.domain.tournament.entity.EventHistory;

import java.time.LocalDate;
import java.util.Optional;

import static org.whitebox.howlook.domain.tournament.entity.QEventHistory.eventHistory;
import static org.whitebox.howlook.domain.tournament.entity.QTournamentHistory.tournamentHistory;


@RequiredArgsConstructor
public class TournamentRepositoryQuerydslImpl implements TournamentRepositoryQuerydsl{
    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<THistoryResponse> findTHistoryResponseByDate(LocalDate date) {
        return Optional.ofNullable(queryFactory
                .select(new QTHistoryResponse(
                        tournamentHistory.T_history_id,
                        tournamentHistory.date,
                        tournamentHistory.lank_1,
                        tournamentHistory.lank_2,
                        tournamentHistory.lank_3,
                        tournamentHistory.lank_4,
                        tournamentHistory.vote_count
                ))
                .from(tournamentHistory)
                .where(tournamentHistory.date.eq(date))
                .fetchOne());
      // return Optional.empty();
    }

    @Override
    public Optional<EHistoryResponse> findEHistoryResponseByDate(LocalDate date) {
        return Optional.ofNullable(queryFactory
                .select(new QEHistoryResponse(
                        eventHistory.E_history_id,
                        eventHistory.date,
                        eventHistory.eventType,
                        eventHistory.lank_1,
                        eventHistory.lank_2,
                        eventHistory.lank_3,
                        eventHistory.lank_4,
                        eventHistory.lank_5,
                        eventHistory.lank_6,
                        eventHistory.lank_7,
                        eventHistory.lank_8,
                        eventHistory.lank_9,
                        eventHistory.lank_10
                ))
                .from(eventHistory)
                .where(eventHistory.date.eq(date))
                .fetchOne());
    }
}
