package org.whitebox.howlook.domain.tournament.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.whitebox.howlook.domain.tournament.dto.QTHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.THistoryResponse;

import java.time.LocalDate;
import java.util.Optional;

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
}
