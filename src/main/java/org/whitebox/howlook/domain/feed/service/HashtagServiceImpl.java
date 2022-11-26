package org.whitebox.howlook.domain.feed.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.feed.dto.HashtagDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.Hashtag;
import org.whitebox.howlook.domain.feed.repository.HashtagRepository;

import javax.transaction.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
@Data
public class HashtagServiceImpl implements HashtagService{
    private final ModelMapper modelMapper;
    private final HashtagRepository hashtagRepository;

    @Override
    public void registerHashtag(HashtagDTO hashtagDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Hashtag hashtag = modelMapper.map(hashtagDTO, Hashtag.class);

        hashtagRepository.save(hashtag);
    }
}