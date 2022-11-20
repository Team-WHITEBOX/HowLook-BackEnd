package org.whitebox.howlook.domain.feed.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
@Data
public class FeedServiceImpl implements  FeedService{

    private final ModelMapper modelMapper;
    private final FeedRepository feedRepository;
    //전달받은 FeedRegisterDTO값을 데이터베이스에 저장
    @Override
    public void register(FeedRegisterDTO feedRegisterDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Feed feed = modelMapper.map(feedRegisterDTO, Feed.class);
        feedRepository.save(feed);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<String> CountTest()
    {
        List<String> cnt = new ArrayList<>();
        cnt.addAll(jdbcTemplate.queryForList("SELECT moddate FROM webdb.feed", String.class));
        return cnt;
    }


}
