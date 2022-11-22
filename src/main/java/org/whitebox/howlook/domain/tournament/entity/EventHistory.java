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
    Long E_history_id;
    LocalDate date;
    String eventType;
    Long lank_1;
    Long lank_2;
    Long lank_3;
    Long lank_4;
    Long lank_5;
    Long lank_6;
    Long lank_7;
    Long lank_8;
    Long lank_9;
    Long lank_10;
}
