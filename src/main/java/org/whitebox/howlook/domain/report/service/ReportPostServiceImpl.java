package org.whitebox.howlook.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.service.PostService;
import org.whitebox.howlook.domain.report.dto.ReportDTO;
import org.whitebox.howlook.domain.upload.dto.PhotoDTO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ReportPostServiceImpl implements ReportPostService{
    private final ModelMapper modelMapper;
    private final PostService postService;
    @Value("${REPORTSERVER_URL}")
    private String reportServer;

    public ResponseEntity<String> reportPost(Long postId) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //post를 조회할 때의 DTO인 postReaderDTO를 우선 postId로 조회해 받아온다.
        PostReaderDTO postReaderDTO = postService.readerPID(postId);

        ReportDTO reportDTO = modelMapper.map(postReaderDTO, ReportDTO.class);
        reportDTO.setMemberId(postReaderDTO.getUserPostInfo().getMemberId());
        reportDTO.setPostReplyCount(postReaderDTO.getReplyCount());

        WebClient webClient = WebClient.builder()
                .baseUrl("http://"+reportServer)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post().uri(uriBuilder -> uriBuilder.path("/report/reportPost")
                .build())
                .bodyValue(reportDTO)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    //public ResponseEntity<String> reportAll(Long postId) throws IOException {
    public void reportAll(Long postId) throws  IOException {
        WebClient webclient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MultipartBodyBuilder builder  = new MultipartBodyBuilder();

        PostReaderDTO postReaderDTO = postService.readerPID(postId);

        ReportDTO reportDTO = modelMapper.map(postReaderDTO, ReportDTO.class);
        reportDTO.setMemberId(postReaderDTO.getUserPostInfo().getMemberId());
        reportDTO.setPostReplyCount(postReaderDTO.getReplyCount());

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("dto", reportDTO);

        parts.add("image", new FileSystemResource(reportDTO.getMainPhotoPath()));

        webclient.post()
                .uri("/report/reportPost")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
