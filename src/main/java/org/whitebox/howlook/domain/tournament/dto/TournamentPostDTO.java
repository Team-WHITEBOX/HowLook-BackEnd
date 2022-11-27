package org.whitebox.howlook.domain.tournament.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TournamentPostDTO {
    Long T_Post_id;
    LocalDate date;
    Long feed_id;
    String photo;
    String member_id;
    Long score;
}
