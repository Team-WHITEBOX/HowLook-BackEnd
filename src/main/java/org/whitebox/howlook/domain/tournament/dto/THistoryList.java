package org.whitebox.howlook.domain.tournament.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class THistoryList {
    Long t_history_id;
    LocalDate date;
    List<Long> lank = new ArrayList<>();
    Long vote_count;

    @QueryProjection
    public THistoryList(Long t_history_id, LocalDate date, Long lank_1, Long lank_2, Long lank_3, Long lank_4, Long vote_count){
        this.t_history_id = t_history_id;
        this.date = date;
        this.lank.add(lank_1);
        this.lank.add(lank_2);
        this.lank.add(lank_3);
        this.lank.add(lank_4);
        this.vote_count = vote_count;
    }

}
