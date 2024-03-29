package org.whitebox.howlook.domain.tournament.service;


import org.whitebox.howlook.domain.tournament.dto.EHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.THistoryList;
import org.whitebox.howlook.domain.tournament.dto.TournamentPostDTO;

import java.time.LocalDate;
import java.util.List;

public interface TournamentService {
    List<TournamentPostDTO> getPosts(LocalDate date);
    void UpdatePosts(List<TournamentPostDTO> postDTOs);

    THistoryList getTHistoryList(LocalDate date);
    EHistoryResponse getEHistory(LocalDate date);
    TournamentPostDTO getPostById(Long postId);
    List<TournamentPostDTO> findTop32postByDateForTourna();
}
