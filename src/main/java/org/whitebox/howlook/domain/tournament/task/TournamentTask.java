package org.whitebox.howlook.domain.tournament.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.post.repository.PostRepository;
import org.whitebox.howlook.domain.tournament.dto.TournamentPostDTO;
import org.whitebox.howlook.domain.tournament.entity.TournamentHistory;
import org.whitebox.howlook.domain.tournament.entity.TournamentPost;
import org.whitebox.howlook.domain.tournament.repository.HistoryRepository;
import org.whitebox.howlook.domain.tournament.repository.TournamentDateRepository;
import org.whitebox.howlook.domain.tournament.repository.TournamentRepository;
import org.whitebox.howlook.domain.tournament.service.TournamentService;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Component
public class TournamentTask {
    private final PostRepository postRepository;
    private final TournamentRepository tournamentRepository;
    private final HistoryRepository historyRepository;
    private final TournamentDateRepository tournamentDateRepository;
    private final TournamentService tournamentService;

    //매일 일반/왕중왕전 비교해서 피드 불러오고 토너먼트 테이블에 저장.
    @Scheduled(cron = "0 30 0 * * *")
    @Transactional
    public void postToTPost() {

        Long lastDay = tournamentDateRepository.selectTournamentDatefromTournamentDateInfo();   //그날의 날짜정보 가져오기
        String tourtype = "Normal";
        if(lastDay % 10 == 0) {  //첫째날에 1부터 저장하므로 10일째에는 10을 읽는다.
            tourtype = "Wangjung";
            tournamentDateRepository.updateTournamentDateReset(); //10번째날 우선 세던날짜를초기화
        }

        List<TournamentPostDTO> posts = tournamentService.findTop32postByDateForTourna();

        if(posts.size() == 32){    //게시글 수가 32개보다 적으면 토너먼트 안함
            String finalTourtype = tourtype;    //tourna_type을 넣기위해서 final형이 대입되어야 하므로.

            for(TournamentPostDTO post : posts)
            {
                TournamentPost tournamentPost = TournamentPost.builder()
                        .date(LocalDate.now()).tournamentPostId(post.getPostId()).photo(post.getPhoto())
                        .memberId(post.getMemberId()).score(0L).tournamentType(finalTourtype).build();

                tournamentRepository.save((tournamentPost));

            }
            //날짜가 초기화된 10일째에 1부터 기록
            tournamentDateRepository.updateTournamentDateToNextDay();
        }
    }

    //Normal tournament history 저장, 4개씩 저장
    @Scheduled(cron = "0 29 0 * * *")
    @Transactional
    public void resultTournamentNormal(){
        Long lastDay = tournamentDateRepository.selectTournamentDatefromTournamentDateInfo();
        if(lastDay % 10 == 1) return;
        List<TournamentPost> posts = tournamentRepository.findTop4ByDateOrderByScoreDesc(LocalDate.now().minusDays(1)); //전날 게시글
        if (posts.size() ==0){
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUNT);
        }
        TournamentHistory history = new TournamentHistory(posts);
        historyRepository.save(history);
    }

    //Event Tournament(왕중왕전) 저장, 10개씩 저장.
    @Scheduled(cron = "0 29 0 * * *")
    @Transactional
    public void resultTournamentWang(){
        Long lastDay = tournamentDateRepository.selectTournamentDatefromTournamentDateInfo();
        if(lastDay % 10 != 1) return;
        List<TournamentPost> posts = tournamentRepository.findTop10ByDateOrderByScoreDesc(LocalDate.now().minusDays(1)); //전날 게시글
        if (posts.size() ==0){
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUNT);
        }
        TournamentHistory history = new TournamentHistory(posts);
        historyRepository.save(history);
    }

}
