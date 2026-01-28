package com.handmade.tle.upload.service;

import com.handmade.tle.shared.dto.PresignedUploadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Validation Service - Security Checks
 * Validates file type, size, and other security constraints.
 */
@Slf4j
@Service
public class ValidationService {

    @Value("${upload.security.max-file-size:10485760}")
    private Long maxFileSize;

    @Value("${upload.security.allowed-content-types:image/jpeg,image/png,image/gif,image/webp,application/pdf}")
    private List<String> allowedContentTypes;

    private static final Set<String> DANGEROUS_EXTENSIONS = Set.of(
            "exe", "bat", "cmd", "sh", "php", "js", "jar");

    public void validateUploadRequest(PresignedUploadRequest request) {
        validateFilename(request.getFilename());
        validateFileSize(request.getFileSize());
        validateContentType(request.getContentType());
        log.debug("Validation passed for file: {}", request.getFilename());
    }

    private void validateFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Filename cannot be empty");
        }
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            throw new IllegalArgumentException("Invalid characters in filename");
        }
        String extension = getFileExtension(filename);
        if (extension != null && DANGEROUS_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("File extension not allowed");
        }
    }

    private void validateFileSize(Long fileSize) {
        if (fileSize == null || fileSize <= 0) {
            throw new IllegalArgumentException("File size must be greater than 0");
        }
        if (fileSize > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds limit of " + maxFileSize + " bytes");
        }
    }

    private void validateContentType(String contentType) {
        if (contentType == null || !allowedContentTypes.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Content type '" + contentType + "' is not allowed");
        }
    }

    private String getFileExtension(String filename) {
        int lastIndex = filename.lastIndexOf('.');
        return (lastIndex == -1) ? null : filename.substring(lastIndex + 1);
    }
}
