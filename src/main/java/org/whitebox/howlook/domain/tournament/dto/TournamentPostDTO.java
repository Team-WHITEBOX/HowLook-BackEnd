package org.whitebox.howlook.domain.tournament.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.tournament.entity.TournamentPost;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TournamentPostDTO {
    private Long tournamentPostId;
    private LocalDate date;
    private Long postId;
    private String photo;
    private String memberId;
    private Long score;
    private String tournamentType;
    private Long LikeCount;     //좋아요개수

    public TournamentPostDTO(Post post)
    {
        this.date = LocalDate.from(post.getRegDate());
        this.postId = post.getPostId();
        this.photo = post.getMainPhotoPath();
        this.memberId = post.getMember().getMemberId();
        this.LikeCount = post.getLikeCount();
    }
    public TournamentPostDTO(TournamentPost tournamentPost){
        this.tournamentPostId = tournamentPost.getTournamentPostId();
        this.date = tournamentPost.getDate();
        this.postId = tournamentPost.getPostId();
        this.photo = tournamentPost.getPhoto();
        this.memberId = tournamentPost.getMemberId();
        this.tournamentType = tournamentPost.getTournamentType();
        this.score = tournamentPost.getScore();
    }
}
