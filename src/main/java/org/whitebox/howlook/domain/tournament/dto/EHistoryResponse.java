package org.whitebox.howlook.domain.tournament.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EHistoryResponse {
    Long e_history_id;
    LocalDate date;
    String eventType;
    Long lank_1;
    Long lank_2;
    Long lank_3;
    Long lank_4;
    Long lank_5;
    Long lank_6;
    Long lank_7;
    Long lank_8;
    Long lank_9;
    Long lank_10;

    @QueryProjection
    public EHistoryResponse(Long e_history_id,LocalDate date,String eventType, Long lank_1,Long lank_2,Long lank_3
            ,Long lank_4,Long lank_5,Long lank_6,Long lank_7,Long lank_8,Long lank_9,Long lank_10){
        this.e_history_id = e_history_id;
        this.date = date;
        this.eventType = eventType;
        this.lank_1 = lank_1;
        this.lank_2 = lank_2;
        this.lank_3 = lank_3;
        this.lank_4 = lank_4;
        this.lank_5 = lank_5;
        this.lank_6 = lank_6;
        this.lank_7 = lank_7;
        this.lank_8 = lank_8;
        this.lank_9 = lank_9;
        this.lank_10 = lank_10;
  }

}
