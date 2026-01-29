package com.handmade.tle.upload.configuration;

import com.handmade.tle.storage.StorageProvider;
import com.handmade.tle.storage.provider.S3StorageProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UploadConfiguration {
    public static final String UPLOAD_DIR = "uploads/";
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    public static final String[] ALLOWED_FILE_TYPES = {"image/jpeg", "image/png", "application/pdf"};

}