package org.whitebox.howlook.domain.tournament.service;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.domain.tournament.dto.EHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.THistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.TournamentPostDTO;
import org.whitebox.howlook.domain.tournament.entity.TournamentPost;
import org.whitebox.howlook.domain.tournament.repository.FeedToTournaRepository;
import org.whitebox.howlook.domain.tournament.repository.TournamentRepository;
import org.whitebox.howlook.global.error.exception.EntityNotFoundException;
import org.whitebox.howlook.global.util.AccountUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.whitebox.howlook.global.error.ErrorCode.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;
    private final FeedToTournaRepository feedToTournaRepository;
    private final FeedRepository feedRepository;
    private final ModelMapper modelMapper;
    private final AccountUtil accountUtil;
    private final MemberRepository memberRepository;

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


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<TournamentPostDTO> findTop32FeedByDateForTourna()
    {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Map<String, Object>> cnt = new ArrayList<>();
        List<Feed> feeds = new ArrayList<>();

        String sql = "SELECT " +
                "DISTINCT (mid),like_count, npost_id, DATE_FORMAT(regdate,'%Y-%m-%d') FROM feed " +
                "WHERE ((mid, like_count) IN (SELECT mid, MAX(like_count) " +
                "FROM feed GROUP BY mid HAVING MAX(like_count))) " +
                "AND " +
                "DATE_FORMAT(NOW() - INTERVAL 3 DAY,'%Y-%m-%d') = DATE_FORMAT(regdate,'%Y-%m-%d') " +
                "ORDER BY like_count DESC, npost_id DESC;";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        cnt.addAll(rows);

        log.info(rows);
        for(Map<String, Object> map: cnt) {
            Feed f = feedRepository.findByPid((Long)map.get("npost_id"));
            f.setMember(memberRepository.findById((String)map.get("mid")).orElseThrow());
            feeds.add(f);
        }

        List<TournamentPostDTO> result = feeds.stream().map(feed ->  new TournamentPostDTO(feed)).collect(Collectors.toList());
        final List<TournamentPostDTO> posts = new ArrayList<>();

        Long last_count = 0L;
        String last_mid = "";
        int count = 0;

        for(TournamentPostDTO feed : result)
        {
            if(feed.getLikeCount() != last_count || !(feed.getMember_id().equals(last_mid)))
            {
                posts.add(feed);
                count += 1;

                if(count >= 32) {
                    break;
                }
            }

            last_count = feed.getLikeCount();
            last_mid = feed.getMember_id();
        }
        return posts;
    }
}
