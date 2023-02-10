package org.whitebox.howlook.domain.tournament.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EHistoryResponse {
    Long eventHistoryId;
    LocalDate date;
    String eventType;
    Long rank_1;
    Long rank_2;
    Long rank_3;
    Long rank_4;
    Long rank_5;
    Long rank_6;
    Long rank_7;
    Long rank_8;
    Long rank_9;
    Long rank_10;

    @QueryProjection
    public EHistoryResponse(Long eventHistoryId, LocalDate date, String eventType, Long rank_1, Long rank_2, Long rank_3
            , Long rank_4, Long rank_5, Long rank_6, Long rank_7, Long rank_8, Long rank_9, Long rank_10){
        this.eventHistoryId = eventHistoryId;
        this.date = date;
        this.eventType = eventType;
        this.rank_1 = rank_1;
        this.rank_2 = rank_2;
        this.rank_3 = rank_3;
        this.rank_4 = rank_4;
        this.rank_5 = rank_5;
        this.rank_6 = rank_6;
        this.rank_7 = rank_7;
        this.rank_8 = rank_8;
        this.rank_9 = rank_9;
        this.rank_10 = rank_10;
  }

}
