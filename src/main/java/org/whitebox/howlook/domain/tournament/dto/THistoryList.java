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
    List<Long> lank = new ArrayList<>();
    Long voteCount;

    @QueryProjection
    public THistoryList(Long tournamentHistoryId, LocalDate date, Long lank_1, Long lank_2, Long lank_3, Long lank_4, Long voteCount){
        this.tournamentHistoryId = tournamentHistoryId;
        this.date = date;
        this.lank.add(lank_1);
        this.lank.add(lank_2);
        this.lank.add(lank_3);
        this.lank.add(lank_4);
        this.voteCount = voteCount;
    }

}
