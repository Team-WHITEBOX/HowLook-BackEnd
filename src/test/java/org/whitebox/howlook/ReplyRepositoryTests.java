package org.whitebox.howlook;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.post.entity.Reply;
import org.whitebox.howlook.domain.post.repository.ReplyRepository;
import org.whitebox.howlook.domain.member.entity.Member;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() { // 참조무결성
        long postId = 100L;

        Post post = Post.builder().postId(postId).build();
        Member member = Member.builder().memberId("replyer").build();

        Reply reply = Reply.builder()
                .post(post)
                .contents("댓글....")
                .member(member)
                .build();

        replyRepository.save(reply);
    }
}
