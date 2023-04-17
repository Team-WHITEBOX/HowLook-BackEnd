package org.whitebox.howlook.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.reactive.function.client.WebClient;
import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.service.PostService;
import org.whitebox.howlook.domain.report.dto.ReportDTO;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportPostServiceImpl implements ReportPostService{
    private final ModelMapper modelMapper;
    private final PostService postService;

    public ResponseEntity<String> reportPost(Long postId) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //post를 조회할 때의 DTO인 postReaderDTO를 우선 postId로 조회해 받아온다.
        PostReaderDTO postReaderDTO = postService.readerPID(postId);

        //PostReaderDTO의 값들 중 필요한 것들을 ReportDTO에 mapping (value 값 다른것들 매핑규칙 적용)
        modelMapper.addMappings(new PropertyMap<PostReaderDTO, ReportDTO>() {
            @Override
            protected void configure() {
                map().setMemberId(postReaderDTO.getUserPostInfo().getMemberId());
                map().setPostReplyCount(postReaderDTO.getReplyCount());
            }
        });
        ReportDTO reportDTO = modelMapper.map(postReaderDTO, ReportDTO.class);

        System.out.println("PostId:" +reportDTO.getPostId());
        System.out.println("MemberId:" +reportDTO.getMemberId());
        System.out.println("PhotoCount:" + reportDTO.getPhotoCount());
        System.out.println("LikeCount:" + reportDTO.getLikeCount());
        System.out.println("ReplyCount:" + reportDTO.getPostReplyCount());
        System.out.println("viewCount:" + reportDTO.getViewCount());
        System.out.println("Content:" + reportDTO.getContent());
        System.out.println("MainPhotoPath:" + reportDTO.getMainPhotoPath());
        System.out.println("photoDTOs:" + reportDTO.getPhotoDTOs());
        System.out.println("registrationDate:" + reportDTO.getRegistrationDate());
        System.out.println("modificationDate:" + reportDTO.getModificationDate());
        System.out.println("Hashtag:" + reportDTO.getHashtagDTO());

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post().uri(uriBuilder -> uriBuilder.path("/report/reportpost")
                .build())
                .bodyValue(reportDTO)
                .retrieve()
                .toEntity(String.class)
                .block();
    }
}
