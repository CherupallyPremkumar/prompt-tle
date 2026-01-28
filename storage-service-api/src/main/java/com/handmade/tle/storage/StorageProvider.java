package com.handmade.tle.storage;

import java.time.Duration;
import java.util.Map;

/**
 * Storage Provider Interface - Strategy Pattern
 * All cloud storage providers must implement this interface.
 * Defined in prompt-storage-api to allow business logic to remain cloud-agnostic.
 */
public interface StorageProvider {
    
    /**
     * Generate presigned URL for upload
     * 
     * @param fileKey Unique key/path for the file
     * @param contentType MIME type
     * @param fileSizeBytes File size
     * @param expiration How long URL is valid
     * @param metadata Additional metadata
     * @return Presigned URL string
     */
    String generatePresignedUploadUrl(
            String fileKey,
            String contentType,
            Long fileSizeBytes,
            Duration expiration,
            Map<String, String> metadata
    );
    
    /**
     * Generate presigned URL for download
     */
    String generatePresignedDownloadUrl(
            String fileKey,
            Duration expiration
    );
    
    /**
     * Check if file exists
     */
    boolean fileExists(String fileKey);
    
    /**
     * Delete file
     */
    void deleteFile(String fileKey);
    
    /**
     * Get file metadata
     */
    Map<String, String> getFileMetadata(String fileKey);
    
    /**
     * Get file size
     */
    Long getFileSize(String fileKey);
    
    /**
     * Get public URL (if bucket allows public access)
     */
    String getPublicUrl(String fileKey);
}
