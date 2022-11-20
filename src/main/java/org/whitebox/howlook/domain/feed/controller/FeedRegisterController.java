package org.whitebox.howlook.domain.feed.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.service.FeedService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/feed")
@Log4j2
@RequiredArgsConstructor
public class FeedRegisterController {
    private final FeedService feedService;

    //게시글 등록하는 POST로 매핑된 API구현
    @PostMapping("/register")
    public void registerPost(@Valid FeedRegisterDTO feedRegisterDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("Feed POST register!");
        if(bindingResult.hasErrors()) {
            log.info("has errors..");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return;
        }

        log.info(feedRegisterDTO);

        feedService.register(feedRegisterDTO);
    }

    @GetMapping("/count")
    public List<String> count()
    {
        //String cnt = "1";
        return feedService.CountTest();
    }
}
