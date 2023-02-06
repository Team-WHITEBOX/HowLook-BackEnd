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
    Long tournamentPostId;
    LocalDate date;
    Long postId;
    String photo;
    String memberId;
    Long score;
    String tournamentType;

    public void setScore(Long score){
        this.score = score;
    }
}
