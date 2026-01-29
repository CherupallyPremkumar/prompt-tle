package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadRepository extends JpaRepository<Upload, String> {
    Optional<Upload> findByUploadId(String uploadId);
}
