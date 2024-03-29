package org.whitebox.howlook.domain.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.tournament.entity.TournamentPost;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TournamentRepository extends JpaRepository<TournamentPost,Long>,TournamentRepositoryQuerydsl {
    Optional<List<TournamentPost>> findByDate(LocalDate date);
    List<TournamentPost> findTop4ByDateOrderByScoreDesc(LocalDate date);

    List<TournamentPost> findTop10ByDateOrderByScoreDesc(LocalDate date);
}
