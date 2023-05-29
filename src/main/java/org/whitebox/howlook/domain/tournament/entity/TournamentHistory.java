package org.whitebox.howlook.domain.tournament.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private Long tournamentHistoryId;
    private LocalDate date;
    private Long rank_1;
    private Long rank_2;
    private Long rank_3;
    private Long rank_4;
    private Long voteCount;

    public TournamentHistory(List<TournamentPost> posts){
        this.date = posts.get(0).getDate();
        this.rank_1 = posts.get(0).getTournamentPostId();
        this.rank_2 = posts.get(1).getTournamentPostId();
        this.rank_3 = posts.get(2).getTournamentPostId();
        this.rank_4 = posts.get(3).getTournamentPostId();
        this.voteCount = posts.stream().mapToLong(post -> post.getScore()).sum();
    }
}
