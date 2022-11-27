package org.whitebox.howlook;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.whitebox.howlook.domain.feed.dto.ReplyDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.entity.Reply;
import org.whitebox.howlook.domain.feed.repository.ReplyRepository;
import org.whitebox.howlook.domain.feed.service.ReplyService;
import org.whitebox.howlook.domain.member.entity.Member;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() { // 참조무결성
        long NpostId = 100L;

        Feed feed = Feed.builder().NPostId(NpostId).build();
        Member member = Member.builder().mid("replyer").build();

        Reply reply = Reply.builder()
                .feed(feed)
                .contents("댓글....")
                .member(member)
                .build();

        replyRepository.save(reply);
    }
}
