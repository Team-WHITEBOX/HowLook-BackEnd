package org.whitebox.howlook.domain.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.tournament.entity.TournamentDateInfo;

import javax.transaction.Transactional;

public interface TournamentDateRepository extends JpaRepository<TournamentDateInfo,Long>{

    @Query("select ti.tournamentDate from TournamentDateInfo ti where ti.dateInfoId = 1")
    Long selectTournamentDatefromTournamentDateInfo();

    //update문 @Query로 작성시 @Modifying @Transactional 붙여줘야 작동가능
    @Modifying
    @Transactional
    @Query("update TournamentDateInfo ti set ti.tournamentDate = ti.tournamentDate + 1 where ti.dateInfoId = 1")
    void updateTournamentDateToNextDay();

    @Modifying
    @Transactional
    @Query("update TournamentDateInfo ti set ti.tournamentDate = 0 where ti.dateInfoId = 1")
    void updateTournamentDateReset();
}
