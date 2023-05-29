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
public class EventHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventHistoryId;
    private LocalDate date;
    private String eventType;
    private Long rank_1;
    private Long rank_2;
    private Long rank_3;
    private Long rank_4;
    private Long rank_5;
    private Long rank_6;
    private Long rank_7;
    private Long rank_8;
    private Long rank_9;
    private Long rank_10;
}
