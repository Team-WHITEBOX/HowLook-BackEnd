package org.whitebox.howlook.domain.feed.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.service.FeedService;
import org.whitebox.howlook.domain.member.service.MemberService;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;

import static org.whitebox.howlook.global.result.ResultCode.CREATE_POST_FAIL;
import static org.whitebox.howlook.global.result.ResultCode.REGISTER_SUCCESS;

@RestController
@RequestMapping("/feed")
@Log4j2
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    //게시글 등록하는 POST로 매핑된 API구현
    @PostMapping(value = "/register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse> registerPost(@Valid @ModelAttribute FeedRegisterDTO feedRegisterDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("Feed POST register!");
        if(bindingResult.hasErrors()) {
            log.info("has errors..");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return ResponseEntity.ok(ResultResponse.of(CREATE_POST_FAIL, false));
        }

        feedService.register(feedRegisterDTO);

        return ResponseEntity.ok(ResultResponse.of(REGISTER_SUCCESS, true));
    }

    
    //게시물 불러오는 GET으로 매핑한 API구현해야함
    @GetMapping("/read")
    public FeedReaderDTO readFeed(Long NPostId) {
        FeedReaderDTO feedReaderDTO = feedService.reader(NPostId);

        log.info(feedReaderDTO);

        return feedReaderDTO;
    }
}
