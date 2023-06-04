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

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentPostId;
    private LocalDate date;
    private Long postId;
    private String photo;
    private String memberId;
    private Long score;
    private String tournamentType;

    public void setScore(Long score){
        this.score = score;
    }
}
