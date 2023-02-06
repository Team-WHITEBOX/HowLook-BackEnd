package org.whitebox.howlook.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.whitebox.howlook.domain.post.entity.Post;
import org.whitebox.howlook.domain.post.entity.PostLike;
import org.whitebox.howlook.domain.post.repository.PostLikeRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeServiceImpl {

    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void deleteAll(Post post) {
        final List<PostLike> postLikes = postLikeRepository.findAllByPost(post);
        postLikeRepository.deleteAllInBatch(postLikes);
    }

    public List<PostLike> getAllWithMember(Long postId) {
        return postLikeRepository.findAllWithMemberByPostId(postId);
    }
}
