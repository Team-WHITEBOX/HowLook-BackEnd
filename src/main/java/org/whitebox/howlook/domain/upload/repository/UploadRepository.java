package org.whitebox.howlook.domain.upload.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.whitebox.howlook.domain.feed.entity.Feed;
import org.whitebox.howlook.domain.upload.entity.Upload;

import java.util.List;

public interface UploadRepository extends JpaRepository<Upload, Long> {
    @Query("select u from Upload u where u.PhotoId = :photoId")
    Upload findByPhotoId(Long photoId);
    @Query("select u from Upload u where u.feed.NPostId = :postId")
    List<Upload> findByPostId(Long postId);
}
