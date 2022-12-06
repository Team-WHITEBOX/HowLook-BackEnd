package org.whitebox.howlook.domain.tournament.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;
import org.whitebox.howlook.domain.tournament.entity.TournamentHistory;
import org.whitebox.howlook.domain.tournament.entity.TournamentPost;
import org.whitebox.howlook.domain.tournament.repository.HistoryRepository;
import org.whitebox.howlook.domain.tournament.repository.TournamentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Component
public class TournamentTask {
    private final FeedRepository feedRepository;
    private final TournamentRepository tournamentRepository;
    private final HistoryRepository historyRepository;

    public void feedToTPost() {
        List<Feed> feeds = feedRepository.findAll();  //모두 가져옴 -> 수정필요
        if(feeds.size() == 32){    //게시글 수가 32개보다 적으면 토너먼트 안함
            feeds.forEach(feed -> {
                TournamentPost tournamentPost = TournamentPost.builder()
                        .date(LocalDate.now()).feed_id(feed.getNPostId()).photo(feed.getMainPhotoPath()).member_id(feed.getMember().getMid()).build();
                tournamentRepository.save((tournamentPost));
            });
        }
    }

    public void resultTournament(){
        List<TournamentPost> posts = tournamentRepository.findTop4ByDateOrderByScoreDesc(LocalDate.now().minusDays(1)); //전날 게시글
        TournamentHistory history = new TournamentHistory(posts);
        historyRepository.save(history);
    }
}
