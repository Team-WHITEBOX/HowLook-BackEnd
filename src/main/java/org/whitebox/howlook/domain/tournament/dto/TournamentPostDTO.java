package org.whitebox.howlook.domain.tournament.dto;

import lombok.Getter;
import org.whitebox.howlook.domain.feed.entity.Feed;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TournamentPostDTO {
    Long t_post_id;
    LocalDateTime date;
    Long feed_id;
    String photo;
    String member_id;
    Long score;

    public TournamentPostDTO(Feed feed)
    {
        this.date = feed.getRegDate();
        this.feed_id = feed.getNPostId();
        this.photo = feed.getMainPhotoPath();
        this.member_id = feed.getMember().getMid();
    }
}
