package org.whitebox.howlook.domain.post.service;

import org.springframework.data.domain.Page;
import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.dto.PostRegisterDTO;
import org.whitebox.howlook.domain.post.dto.HashtagDTO;

import java.util.List;

public interface PostService {
    List<String> registerPOST(PostRegisterDTO postRegisterDTO);
    PostReaderDTO readerPID(Long postId);

    List<PostReaderDTO> readerUID(String UserID);

    Page<PostReaderDTO> getpostPage(int size, int page);

    Page<PostReaderDTO> getNearpostPage(int size, int page, float latitude, float longitude);

    public void scrappost(Long postId);

    public void unScrappost(Long postId);

    List<PostReaderDTO> searchpostByHashtag(HashtagDTO hashtagDTO, Long heightHigh, Long heightLow, Long weightHigh, Long weightLow, char gender, int page, int size);

    public void deletepost(Long postId);

    public void likepost(Long postId);

    public void unlikepost(Long postId);
}
