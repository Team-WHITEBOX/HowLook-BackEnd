package org.whitebox.howlook.domain.tournament.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.whitebox.howlook.domain.tournament.entity.TournamentPost;

public interface TournamentRepository extends JpaRepository<TournamentPost,Long>,TournamentRepositoryQuerydsl {
}
