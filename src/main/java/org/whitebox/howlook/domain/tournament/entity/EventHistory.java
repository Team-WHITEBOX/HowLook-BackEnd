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
public class EventHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long eventHistoryId;
    LocalDate date;
    String eventType;
    Long rank_1;
    Long rank_2;
    Long rank_3;
    Long rank_4;
    Long rank_5;
    Long rank_6;
    Long rank_7;
    Long rank_8;
    Long rank_9;
    Long rank_10;
}
