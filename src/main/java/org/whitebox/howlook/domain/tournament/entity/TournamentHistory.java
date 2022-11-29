package org.whitebox.howlook.domain.tournament.entity;

import com.jayway.jsonpath.internal.function.numeric.Sum;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString
public class TournamentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long T_history_id;
    LocalDate date;
    Long lank_1;
    Long lank_2;
    Long lank_3;
    Long lank_4;
    Long vote_count;

    public TournamentHistory(List<TournamentPost> posts){
        this.date = posts.get(0).getDate();
        this.lank_1 = posts.get(0).getT_Post_id();
        this.lank_2 = posts.get(1).getT_Post_id();
        this.lank_3 = posts.get(2).getT_Post_id();
        this.lank_4 = posts.get(3).getT_Post_id();
        this.vote_count = posts.stream().mapToLong(i -> i.score).sum();
    }
}
