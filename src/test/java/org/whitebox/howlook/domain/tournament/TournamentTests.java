package org.whitebox.howlook.domain.tournament;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.whitebox.howlook.domain.tournament.task.TournamentTask;

@SpringBootTest
@Log4j2
public class TournamentTests {
    @Autowired
    private TournamentTask tournamentTask;
    @Test
    public void taskTest(){
        tournamentTask.postToTPost();
    }
    @Test
    public void taskTest2(){
        tournamentTask.resultTournamentNormal();
    }
}
