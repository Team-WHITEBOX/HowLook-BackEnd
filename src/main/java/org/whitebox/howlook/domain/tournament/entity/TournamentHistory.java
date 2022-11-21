package org.whitebox.howlook.domain.tournament.entity;

import lombok.*;

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
}
