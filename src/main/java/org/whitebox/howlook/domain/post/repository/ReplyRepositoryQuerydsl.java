package org.whitebox.howlook.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.whitebox.howlook.domain.post.dto.ReplyReadDTO;
import org.whitebox.howlook.domain.post.entity.Post;

public interface ReplyRepositoryQuerydsl{
    public Page<ReplyReadDTO> findAllByPost(Pageable pageable, Post post);
}
