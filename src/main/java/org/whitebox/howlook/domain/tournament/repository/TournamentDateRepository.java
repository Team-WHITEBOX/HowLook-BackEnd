package org.whitebox.howlook.domain.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.tournament.entity.TournamentDateInfo;

public interface TournamentDateRepository extends JpaRepository<TournamentDateInfo,Long>{
    Long findTournamentDateInfoByConfigdateid(Long id);

    void deleteAll();
}
