package org.whitebox.howlook.domain.feed;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.BootstrapRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.Hashtag;
import org.whitebox.howlook.domain.feed.repository.FeedRepository;
import org.whitebox.howlook.domain.feed.repository.HashtagRepository;
import org.whitebox.howlook.domain.member.entity.Member;
import org.whitebox.howlook.domain.member.repository.MemberRepository;


import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class FeedTests {
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Test
    @Transactional
    public void registerPostTest(){
//        Member member = memberRepository.findByMid("pppp").orElseThrow();
//        IntStream.rangeClosed(1,100).forEach(i-> {
//            Hashtag hashtag = Hashtag.builder().HashtagId((long) (i+10)).amekaji(true).casual(true).street(false).guitar(false).minimal(false).sporty(true).build();
//            log.info(hashtag);
//            Feed feed = Feed.builder().NPostId((long) (i+10)).Content("test"+i).member(member).build();
//            log.info(feed);
//            hashtag.setFeed(feed);
//            log.info(hashtag);
//            hashtagRepository.save(hashtag);
//            feed.setHashtag(hashtag);
//            log.info(feed);
//            feedRepository.save(feed);
//        });
    }
}
