package org.whitebox.howlook.domain.tournament.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long t_post_id;
    LocalDate date;
    Long feed_id;
    String photo;
    String member_id;
    Long score;

    public void setScore(Long score){
        this.score = score;
    }
}
