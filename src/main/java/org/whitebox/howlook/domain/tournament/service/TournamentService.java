package org.whitebox.howlook.domain.tournament.service;


import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.tournament.dto.EHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.THistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.TournamentPostDTO;
import org.whitebox.howlook.domain.tournament.entity.TournamentHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TournamentService {
    List<TournamentPostDTO> getPosts(LocalDate date);
    void UpdatePosts(List<TournamentPostDTO> postDTOs);
    THistoryResponse getTHistory(LocalDate date);
    EHistoryResponse getEHistory(LocalDate date);
    TournamentPostDTO getPostById(Long postId);
    List<TournamentPostDTO> findTop32FeedByDateForTourna();
}
