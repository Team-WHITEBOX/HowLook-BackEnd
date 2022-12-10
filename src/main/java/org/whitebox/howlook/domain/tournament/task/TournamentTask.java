package org.whitebox.howlook.domain.tournament.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;
import org.whitebox.howlook.domain.tournament.dto.TournamentPostDTO;
import org.whitebox.howlook.domain.tournament.entity.TournamentHistory;
import org.whitebox.howlook.domain.tournament.entity.TournamentPost;
import org.whitebox.howlook.domain.tournament.repository.HistoryRepository;
import org.whitebox.howlook.domain.tournament.repository.TournamentDateRepository;
import org.whitebox.howlook.domain.tournament.repository.TournamentRepository;
import org.whitebox.howlook.domain.tournament.service.TournamentService;

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
    private final TournamentService tournamentService;

    //매일 일반/왕중왕전 비교해서 피드 불러오고 토너먼트 테이블에 저장.
    @Scheduled(cron = "0 30 0 * * *")
    public void feedToTPost() {
        Long lastDay = tournamentDateRepository.selectTournamentDatefromTournamentDateInfo();   //그날의 날짜정보 가져오기
        String tourtype = "Normal";
        if(lastDay % 10 == 0) {  //첫째날에 1부터 저장하므로 10일째에는 10을 읽는다.
            tourtype = "Wangjung";
            tournamentDateRepository.updateTournamentDateReset(); //10번째날 우선 세던날짜를초기화
        }

        List<TournamentPostDTO> feeds = tournamentService.findTop32FeedByDateForTourna();
        if(feeds.size() == 32){    //게시글 수가 32개보다 적으면 토너먼트 안함
            String finalTourtype = tourtype;    //tourna_type을 넣기위해서 final형이 대입되어야 하므로.
            feeds.forEach(feed -> {
                TournamentPost tournamentPost = TournamentPost.builder()
                        .date(LocalDate.now()).feed_id(feed.getFeed_id()).photo(feed.getPhoto())
                        .member_id(feed.getMember_id()).tourna_type(finalTourtype).build();
                tournamentRepository.save((tournamentPost));
            });
        }

        tournamentRepository.save(TournamentPost.builder().tourna_type(tourtype).build());
        //날짜가 초기화된 10일째에 1부터 기록
        tournamentDateRepository.updateTournamentDateToNextDay();
    }

    //Normal tournament history 저장, 4개씩 저장
    @Scheduled(cron = "0 29 0 * * *")
    public void resultTournamentNormal(){
        Long lastDay = tournamentDateRepository.selectTournamentDatefromTournamentDateInfo();
        if(lastDay % 10 == 1) return;
        List<TournamentPost> posts = tournamentRepository.findTop4ByDateOrderByScoreDesc(LocalDate.now().minusDays(1)); //전날 게시글
        TournamentHistory history = new TournamentHistory(posts);
        historyRepository.save(history);
    }

    //Event Tournament(왕중왕전) 저장, 10개씩 저장.
    @Scheduled(cron = "0 29 0 * * *")
    public void resultTournamentWang(){
        Long lastDay = tournamentDateRepository.selectTournamentDatefromTournamentDateInfo();
        if(lastDay % 10 != 1) return;
        List<TournamentPost> posts = tournamentRepository.findTop10ByDateOrderByScoreDesc(LocalDate.now().minusDays(1)); //전날 게시글
        TournamentHistory history = new TournamentHistory(posts);
        historyRepository.save(history);
    }

}
