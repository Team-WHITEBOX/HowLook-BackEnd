package org.whitebox.howlook.domain.tournament.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class TournamentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long tournamentHistoryId;
    LocalDate date;
    Long rank_1;
    Long rank_2;
    Long rank_3;
    Long rank_4;
    Long voteCount;

    public TournamentHistory(List<TournamentPost> posts){
        this.date = posts.get(0).getDate();
        this.rank_1 = posts.get(0).getTournamentPostId();
        this.rank_2 = posts.get(1).getTournamentPostId();
        this.rank_3 = posts.get(2).getTournamentPostId();
        this.rank_4 = posts.get(3).getTournamentPostId();
        this.voteCount = posts.stream().mapToLong(i -> i.score).sum();
    }
}
