package org.whitebox.howlook.domain.feed.controller;

import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.whitebox.howlook.domain.feed.dto.FeedReaderDTO;
import org.whitebox.howlook.domain.feed.dto.FeedRegisterDTO;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.feed.service.FeedService;
import org.whitebox.howlook.domain.member.service.MemberService;
import org.whitebox.howlook.global.error.ErrorCode;
import org.whitebox.howlook.global.error.ErrorResponse;
import org.whitebox.howlook.global.result.ResultResponse;

import javax.validation.Valid;
import javax.xml.transform.Result;

import java.util.List;
import java.util.Map;

import static org.whitebox.howlook.global.error.ErrorCode.POST_CANT_UPLOAD;
import static org.whitebox.howlook.global.result.ResultCode.*;

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
    public ResponseEntity<ResultResponse> registerPost(@Valid @ModelAttribute FeedRegisterDTO feedRegisterDTO) {
        log.info("Feed POST register!");

        feedService.register(feedRegisterDTO);
        return ResponseEntity.ok(ResultResponse.of(CREATE_POST_SUCCESS));
    }

    
    //게시물 불러오는 GET으로 매핑한 API구현해야함
    @GetMapping("/readbypid")
    public ResponseEntity<ResultResponse> readFeedbyPID(Long NPostId) {
        FeedReaderDTO feedReaderDTO = feedService.readerPID(NPostId);

        log.info(feedReaderDTO);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS, feedReaderDTO));
    }


    @GetMapping("/readbyuid")
    public ResponseEntity<ResultResponse> readFeedbyUID(String UserID) {
        List<FeedReaderDTO> feeds = feedService.readerUID(UserID);

        log.info(feeds);

        return ResponseEntity.ok(ResultResponse.of(FIND_POST_SUCCESS,feeds));
    }
}
