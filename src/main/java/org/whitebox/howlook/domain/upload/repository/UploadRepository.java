package org.whitebox.howlook.domain.upload.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.whitebox.howlook.domain.upload.entity.Upload;

public interface UploadRepository extends JpaRepository<Upload, Long> {
}
