package org.whitebox.howlook.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.whitebox.howlook.domain.evaluation.repository.EvalRepository;

@SpringBootTest
@Log4j2
public class EvalRepositoryTests {
    @Autowired
    private EvalRepository evalRepository;
    @Test
    public void testSearch1()
    {
        Pageable pageable = PageRequest.of(1,10, Sort.by("NPostId").descending());

        evalRepository.search1(pageable);

    }
}
