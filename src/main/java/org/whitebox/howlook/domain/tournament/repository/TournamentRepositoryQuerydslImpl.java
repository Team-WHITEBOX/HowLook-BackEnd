package org.whitebox.howlook.domain.tournament.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.whitebox.howlook.domain.tournament.dto.EHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.QEHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.QTHistoryList;
import org.whitebox.howlook.domain.tournament.dto.THistoryList;

import java.time.LocalDate;
import java.util.Optional;

import static org.whitebox.howlook.domain.tournament.entity.QEventHistory.eventHistory;
import static org.whitebox.howlook.domain.tournament.entity.QTournamentHistory.tournamentHistory;


@RequiredArgsConstructor
public class TournamentRepositoryQuerydslImpl implements TournamentRepositoryQuerydsl{
    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<THistoryList> findTHistoryListByDate(LocalDate date) {
        return Optional.ofNullable(queryFactory
                .select(new QTHistoryList(
                        tournamentHistory.tournamentHistoryId,
                        tournamentHistory.date,
                        tournamentHistory.rank_1,
                        tournamentHistory.rank_2,
                        tournamentHistory.rank_3,
                        tournamentHistory.rank_4,
                        tournamentHistory.voteCount
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
                        eventHistory.eventHistoryId,
                        eventHistory.date,
                        eventHistory.eventType,
                        eventHistory.rank_1,
                        eventHistory.rank_2,
                        eventHistory.rank_3,
                        eventHistory.rank_4,
                        eventHistory.rank_5,
                        eventHistory.rank_6,
                        eventHistory.rank_7,
                        eventHistory.rank_8,
                        eventHistory.rank_9,
                        eventHistory.rank_10
                ))
                .from(eventHistory)
                .where(eventHistory.date.eq(date))
                .fetchOne());
    }
}