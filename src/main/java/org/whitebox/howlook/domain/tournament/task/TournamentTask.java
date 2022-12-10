package org.whitebox.howlook.domain.tournament.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;
import org.whitebox.howlook.domain.tournament.entity.TournamentDateInfo;
import org.whitebox.howlook.domain.tournament.entity.TournamentHistory;
import org.whitebox.howlook.domain.tournament.entity.TournamentPost;
import org.whitebox.howlook.domain.tournament.repository.HistoryRepository;
import org.whitebox.howlook.domain.tournament.repository.TournamentDateRepository;
import org.whitebox.howlook.domain.tournament.repository.TournamentRepository;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Component
public class TournamentTask {
    private final FeedRepository feedRepository;
    private final TournamentRepository tournamentRepository;
    private final HistoryRepository historyRepository;
    private final TournamentDateRepository tournamentDateRepository;

    @Scheduled(cron = "0 30 0 * * *")
    public void feedToTPost() {
        Long lastday = tournamentDateRepository.findTournamentDateInfoByConfigdateid(1L);
        String tourtype = "Normal";
        if(lastday % 9 == 0) {
            tourtype = "Kingjung";
            tournamentDateRepository.deleteAll();   //왕중왕전 하는 날에 세던 날짜를 초기화
            lastday = 0L;
        }

        List<Feed> feeds = feedRepository.findAll();  //모두 가져옴 -> 수정필요
        if(feeds.size() == 32){    //게시글 수가 32개보다 적으면 토너먼트 안함
            String finalTourtype = tourtype;    //tourna_type을 넣기위해서 final형이 대입되어야 하므로.
            feeds.forEach(feed -> {
                TournamentPost tournamentPost = TournamentPost.builder()
                        .date(LocalDate.now()).feed_id(feed.getNPostId()).photo(feed.getMainPhotoPath())
                        .member_id(feed.getMember().getMid()).tourna_type(finalTourtype).build();
                tournamentRepository.save((tournamentPost));
            });
        }

        TournamentDateInfo.builder().tournamentDate(++lastday).build();
    }

    @Scheduled(cron = "0 19 * * * *")
    public void resultTournament(){
        List<TournamentPost> posts = tournamentRepository.findTop4ByDateOrderByScoreDesc(LocalDate.now().minusDays(1)); //전날 게시글
        TournamentHistory history = new TournamentHistory(posts);
        historyRepository.save(history);
    }
}
