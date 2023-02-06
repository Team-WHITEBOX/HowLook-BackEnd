package org.whitebox.howlook.domain.post;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.whitebox.howlook.domain.post.repository.PostRepository;
import org.whitebox.howlook.domain.post.repository.HashtagRepository;
import org.whitebox.howlook.domain.member.repository.MemberRepository;

@SpringBootTest
@Log4j2
public class postTests {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Test
    @Transactional
    public void registerPostTest(){
//        Member member = memberRepository.findBymemberId("pppp").orElseThrow();
//        IntStream.rangeClosed(1,100).forEach(i-> {
//            Hashtag hashtag = Hashtag.builder().HashtagId((long) (i+10)).amekaji(true).casual(true).street(false).guitar(false).minimal(false).sporty(true).build();
//            log.info(hashtag);
//            post post = post.builder().postId((long) (i+10)).Content("test"+i).member(member).build();
//            log.info(post);
//            hashtag.setpost(post);
//            log.info(hashtag);
//            hashtagRepository.save(hashtag);
//            post.setHashtag(hashtag);
//            log.info(post);
//            postRepository.save(post);
//        });
    }
}
