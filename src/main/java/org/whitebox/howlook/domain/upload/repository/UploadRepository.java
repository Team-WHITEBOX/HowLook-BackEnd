package org.whitebox.howlook.domain.upload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.whitebox.howlook.domain.upload.entity.Upload;

import java.util.List;

public interface UploadRepository extends JpaRepository<Upload, Long> {
    @Query("select u from Upload u where u.uploadId = :photoId")
    Upload findByPhotoId(Long photoId);
    @Query("select u from Upload u where u.post.postId = :postId")
    List<Upload> findByPostId(Long postId);
}
