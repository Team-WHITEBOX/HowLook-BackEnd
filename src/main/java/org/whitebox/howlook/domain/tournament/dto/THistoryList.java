package org.whitebox.howlook.domain.tournament.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class THistoryList {
    Long tournamentHistoryId;
    LocalDate date;
    List<Long> rank = new ArrayList<>();
    Long voteCount;

    @QueryProjection
    public THistoryList(Long tournamentHistoryId, LocalDate date, Long rank_1, Long rank_2, Long rank_3, Long rank_4, Long voteCount){
        this.tournamentHistoryId = tournamentHistoryId;
        this.date = date;
        this.rank.add(rank_1);
        this.rank.add(rank_2);
        this.rank.add(rank_3);
        this.rank.add(rank_4);
        this.voteCount = voteCount;
    }

}
