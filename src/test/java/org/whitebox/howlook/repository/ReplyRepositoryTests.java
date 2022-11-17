package org.whitebox.howlook.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.whitebox.howlook.domain.Reply.dto.ReplyDTO;
import org.whitebox.howlook.domain.Reply.reply;
import org.whitebox.howlook.domain.Reply.repository.ReplyRepository;
import org.whitebox.howlook.domain.Reply.service.ReplyService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ReplyService replyService;

    @Test
    public void testInsert() { // DB에 데이터 insert 되는지 확인

        int bno = 100;

        reply r = reply.builder()
                .NPostId(100)
                .UserId("User1")
                .Contents("안녕하세요")
                .LikeCount(0)
                .Date(LocalDateTime.now())
                .ParentsId(1)
                .build();

        replyRepository.save(r);
    }

    @Test
    @Transactional
    public void testBoardReplies() { // 댓글 데이터 확인
        int bno = 100;
        Pageable pageable = PageRequest.of(0,10, Sort.by("CommendId").descending());

        Page<reply> result = replyRepository.listofBoard(bno, pageable);

        result.getContent().forEach(reply -> {log.info(reply);});
    }




    @Test
    public void testRegister() {
        ReplyDTO replyDTO = ReplyDTO.builder()
                .NPostId(100)
                .UserId("User1")
                .Contents("안녕!")
                .LikeCount(1)
                .Date(LocalDateTime.now())
                .ParentsId(1)
                .build();

        log.info(replyService.register(replyDTO));
    }
}
