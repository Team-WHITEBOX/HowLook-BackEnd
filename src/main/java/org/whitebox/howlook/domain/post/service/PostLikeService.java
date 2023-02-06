package org.whitebox.howlook.domain.post.service;

import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.post.entity.PostLike;

import java.util.List;

public interface PostLikeService {
    void deleteAll(Post post);
    List<PostLike> getAllWithMember(Long postId);
}
