package org.whitebox.howlook.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.post.repository.PostRepository;
import org.whitebox.howlook.domain.post.service.PostService;
import org.whitebox.howlook.domain.report.dto.ReportDTO;
import org.whitebox.howlook.global.util.AccountUtil;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportPostServiceImpl implements ReportPostService{
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final PostService postService;
    private final AccountUtil accountUtil;
    private final RestTemplate restTemplate;

    public void report(Long postId) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //post를 조회할 때의 DTO인 postReaderDTO를 우선 postId로 조회해 받아온다.
        PostReaderDTO postReaderDTO = postService.readerPID(postId);

        //PostReaderDTO의 값들 중 필요한 것들을 ReportDTO에 mapping (value 값 다른것들 매핑규칙 적용)
        modelMapper.addMappings(new PropertyMap<PostReaderDTO, ReportDTO>() {
            @Override
            protected void configure() {
                map().setMember(postReaderDTO.getUserPostInfo().getMemberId());
                map().setPostReplyCount(postReaderDTO.getReplyCount());
            }
        });
        ReportDTO reportDTO = modelMapper.map(postReaderDTO, ReportDTO.class);

        String url = "http://localhost:8080/report/reportpost";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ReportDTO> request = new HttpEntity<>(reportDTO, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to send DTO to B server");
        }


    }
}
