package org.whitebox.howlook.domain.tournament.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class THistoryResponse {
    Long t_history_id;
    LocalDate date;

    List<TournamentPostDTO> postDTOS;
    Long vote_count;

    public THistoryResponse(THistoryList tHistoryList){
        this.t_history_id = tHistoryList.getT_history_id();
        this.date = tHistoryList.date;
        this.vote_count= tHistoryList.getVote_count();
    }

    public void setPostDTOS(List<TournamentPostDTO> postDTOS) {
        this.postDTOS = postDTOS;
    }
}
