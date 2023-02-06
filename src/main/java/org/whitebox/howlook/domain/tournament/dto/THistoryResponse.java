package org.whitebox.howlook.domain.tournament.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class THistoryResponse {
    Long tournamentHistoryId;
    LocalDate date;

    List<TournamentPostDTO> postDTOS;
    Long voteCount;

    public THistoryResponse(THistoryList tHistoryList){
        this.tournamentHistoryId = tHistoryList.getTournamentHistoryId();
        this.date = tHistoryList.date;
        this.voteCount = tHistoryList.getVoteCount();
    }

    public void setPostDTOS(List<TournamentPostDTO> postDTOS) {
        this.postDTOS = postDTOS;
    }
}
