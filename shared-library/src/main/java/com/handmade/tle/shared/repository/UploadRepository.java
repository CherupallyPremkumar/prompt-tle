package com.handmade.tle.shared.repository;

import com.handmade.tle.shared.model.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploadRepository extends JpaRepository<Upload, String> {
    Optional<Upload> findByUploadId(String uploadId);
}
