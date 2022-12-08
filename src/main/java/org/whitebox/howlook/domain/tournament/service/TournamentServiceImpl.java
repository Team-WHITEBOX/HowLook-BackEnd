package org.whitebox.howlook.domain.tournament.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.tournament.dto.EHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.THistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.TournamentPostDTO;
import org.whitebox.howlook.domain.tournament.entity.TournamentPost;
import org.whitebox.howlook.domain.tournament.repository.FeedToTournaRepository;
import org.whitebox.howlook.domain.tournament.repository.TournamentRepository;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.whitebox.howlook.global.error.ErrorCode.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;
    private final FeedToTournaRepository feedToTournaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<TournamentPostDTO> getPosts(LocalDate date) {
        List<TournamentPost> posts = tournamentRepository.findByDate(date)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUNT));
        // posts 없으면 예외
        //
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<TournamentPostDTO> result = posts.stream().map(post -> modelMapper.map(post,TournamentPostDTO.class)).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<String> getTopPosts()
    {
        List<String> result = feedToTournaRepository.FindTop32FeedByDateForTourna();
        return result;
    }

    @Override
    public void UpdatePosts(List<TournamentPostDTO> postDTOs) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        postDTOs.forEach(p -> {
            TournamentPost post = modelMapper.map(p,TournamentPost.class);
            tournamentRepository.save(post);
        });
    }

    @Override
    public THistoryResponse getTHistory(LocalDate date) {
        THistoryResponse result = tournamentRepository.findTHistoryResponseByDate(date)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUNT));
        return result;
    }

    @Override
    public EHistoryResponse getEHistory(LocalDate date) {
        EHistoryResponse result = tournamentRepository.findEHistoryResponseByDate(date)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUNT));
        return result;
    }

    @Override
    public TournamentPostDTO getPostById(Long postId) {
        TournamentPost post = tournamentRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(POST_NOT_FOUND));
        TournamentPostDTO dto = modelMapper.map(post,TournamentPostDTO.class);
        return dto;
    }
}
