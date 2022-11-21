package org.whitebox.howlook.domain.tournament.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.feed.entity.Feed;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long T_Post_id;
    Long history_id;
    Long feed_id;
    Long photo_id;
    Long member_id;
    Long score;
}
