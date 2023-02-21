package org.whitebox.howlook.domain.post.service;

import org.springframework.data.domain.Page;
import org.whitebox.howlook.domain.post.dto.PostReaderDTO;
import org.whitebox.howlook.domain.post.dto.PostRegisterDTO;
import org.whitebox.howlook.domain.post.dto.SearchCategoryDTO;

import java.util.List;

public interface PostService {
    List<String> registerPOST(PostRegisterDTO postRegisterDTO);
    PostReaderDTO readerPID(Long postId);

    List<PostReaderDTO> readerUID(String UserID);

    Page<PostReaderDTO> getPostPage(int size, int page);

    Page<PostReaderDTO> getNearPostPage(int size, int page, float latitude, float longitude);

    public void scrapPost(Long postId);

    public void unScrapPost(Long postId);

    List<PostReaderDTO> searchPostByHashtag(SearchCategoryDTO searchCategoryDTO);

    public void deletePost(Long postId);

    public void likePost(Long postId);

    public void unlikePost(Long postId);
}
