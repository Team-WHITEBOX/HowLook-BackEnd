package org.whitebox.howlook.domain.tournament.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.NumberPath;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@ApiModel("상시 토너먼트 랭킹 기록 응답 모델")
@Getter
//@Builder
public class THistoryResponse {
    Long T_history_id;
    LocalDate date;
    Long lank_1;
    Long lank_2;
    Long lank_3;
    Long lank_4;
    Long vote_count;

    @QueryProjection
    public THistoryResponse(Long t_history_id,LocalDate date,Long lank_1,Long lank_2,Long lank_3,Long lank_4,Long vote_count){
        this.T_history_id = t_history_id;
        this.date = date;
        this.lank_1 = lank_1;
        this.lank_2 = lank_2;
        this.lank_3 = lank_3;
        this.lank_4 = lank_4;
        this.vote_count = vote_count;
    }

}
