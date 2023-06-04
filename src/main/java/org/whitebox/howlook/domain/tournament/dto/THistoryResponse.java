package org.whitebox.howlook.domain.tournament.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class THistoryResponse {
    private Long tournamentHistoryId;
    private LocalDate date;
    private List<TournamentPostDTO> postDTOS;
    private Long voteCount;

    public THistoryResponse(THistoryList tHistoryList){
        this.tournamentHistoryId = tHistoryList.getTournamentHistoryId();
        this.date = tHistoryList.getDate();
        this.voteCount = tHistoryList.getVoteCount();
    }

    public void setPostDTOS(List<TournamentPostDTO> postDTOS) {
        this.postDTOS = postDTOS;
    }
}
