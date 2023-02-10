package org.whitebox.howlook.domain.tournament.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.post.repository.PostRepository;
import org.whitebox.howlook.domain.member.repository.MemberRepository;
import org.whitebox.howlook.domain.tournament.dto.EHistoryResponse;
import org.whitebox.howlook.domain.tournament.dto.THistoryList;
import org.whitebox.howlook.domain.tournament.dto.TournamentPostDTO;
import org.whitebox.howlook.domain.tournament.entity.TournamentPost;
import org.whitebox.howlook.domain.tournament.repository.postToTournaRepository;
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
    private final postToTournaRepository postToTournaRepository;
    private final PostRepository postRepository;
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
        List<TournamentPostDTO> result = posts.stream().map(post -> new TournamentPostDTO(post)).collect(Collectors.toList());
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
    public THistoryList getTHistoryList(LocalDate date) {
        THistoryList result = tournamentRepository.findTHistoryListByDate(date)
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
        TournamentPostDTO dto = new TournamentPostDTO(post);
        return dto;
    }


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<TournamentPostDTO> findTop32postByDateForTourna()
    {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Map<String, Object>> cnt = new ArrayList<>();
        List<Post> posts = new ArrayList<>();

        String sql = "SELECT " +
                "DISTINCT (memberId),likeCount, postId, DATE_FORMAT(regdate,'%Y-%m-%d') FROM post " +
                "WHERE ((memberId, likeCount) IN (SELECT memberId, MAX(likeCount) " +
                "FROM post GROUP BY memberId HAVING MAX(likeCount))) " +
                "AND " +
                "DATE_FORMAT(NOW() - INTERVAL 1 DAY,'%Y-%m-%d') = DATE_FORMAT(regdate,'%Y-%m-%d') " +
                "ORDER BY likeCount DESC, postId DESC;";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        cnt.addAll(rows);

        log.info(rows);
        for(Map<String, Object> map: cnt) {
            Post f = postRepository.findByPostId((Long)map.get("postId"));
            f.setMember(memberRepository.findById((String)map.get("memberId")).orElseThrow());
            posts.add(f);
        }

        List<TournamentPostDTO> result = posts.stream().map(post ->  new TournamentPostDTO(post)).collect(Collectors.toList());
        final List<TournamentPostDTO> tournamentPosts = new ArrayList<>();

        Long last_count = 0L;
        String last_memberId = "";
        int count = 0;

        for(TournamentPostDTO post : result)
        {
            if(post.getLikeCount() != last_count || !(post.getMemberId().equals(last_memberId)))
            {
                tournamentPosts.add(post);
                count += 1;

                if(count >= 32) {
                    break;
                }
            }

            last_count = post.getLikeCount();
            last_memberId = post.getMemberId();
        }
        return tournamentPosts;
    }
}
