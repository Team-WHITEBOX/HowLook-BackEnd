package org.whitebox.howlook.domain.tournament.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDateInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long dateInfoId;

    Long tournamentDate;
}
