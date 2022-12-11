package org.whitebox.howlook.domain.tournament.dto;

import lombok.Getter;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.tournament.entity.TournamentPost;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TournamentPostDTO {
    Long t_post_id;
    LocalDate date;
    Long feed_id;
    String photo;
    String member_id;
    Long score;
    String tourna_type;
    Long LikeCount;     //좋아요개수

    public TournamentPostDTO(Feed feed)
    {
        this.date = LocalDate.from(feed.getRegDate());
        this.feed_id = feed.getNPostId();
        this.photo = feed.getMainPhotoPath();
        this.member_id = feed.getMember().getMid();
        this.LikeCount = feed.getLikeCount();
    }
    public TournamentPostDTO(TournamentPost tournamentPost){
        this.t_post_id = tournamentPost.getT_post_id();
        this.date = tournamentPost.getDate();
        this.feed_id = tournamentPost.getFeed_id();
        this.photo = tournamentPost.getPhoto();
        this.member_id = tournamentPost.getMember_id();
        this.tourna_type = tournamentPost.getTourna_type();
        this.score = tournamentPost.getScore();
    }
}
