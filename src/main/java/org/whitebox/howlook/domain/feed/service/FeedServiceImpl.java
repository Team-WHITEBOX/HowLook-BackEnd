package org.whitebox.howlook.domain.feed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
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

    @Override
    public FeedReaderDTO reader(Long NPostId) {
        Optional<Feed> result = feedRepository.findById(NPostId);

        Feed feed = result.orElseThrow();
        log.info(feed);

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        FeedReaderDTO feedReaderDTO = modelMapper.map(feed, FeedReaderDTO.class);

        return feedReaderDTO;
    }
}
