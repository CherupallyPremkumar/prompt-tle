package com.handmade.tle.storage.configuration;

import com.handmade.tle.storage.StorageProvider;
import com.handmade.tle.storage.provider.GCSStorageProvider;
import com.handmade.tle.storage.provider.S3StorageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

    @Value("${cloud.storage.s3.region:us-east-1}")
    private String s3Region;

    @Value("${cloud.storage.s3.bucketName:}")
    private String s3BucketName;

    @Value("${cloud.storage.s3.access-key:}")
    private String s3AccessKey;

    @Value("${cloud.storage.s3.secret-key:}")
    private String s3SecretKey;

    @Value("${cloud.storage.gcs.project-id:}")
    private String gcsProjectId;

    @Value("${cloud.storage.gcs.bucket:}")
    private String gcsBucketName;

    @Value("${cloud.storage.gcs.credentials-path:}")
    private String gcsCredentialsPath;

    @Bean
    @ConditionalOnProperty(name = "cloud.storage.provider", havingValue = "s3")
    public StorageProvider s3StorageProvider() {
        return new S3StorageProvider(s3Region, s3BucketName, s3AccessKey, s3SecretKey);
    }

    @Bean
    @ConditionalOnProperty(name = "cloud.storage.provider", havingValue = "gcs")
    public StorageProvider gcsStorageProvider() {
        return new GCSStorageProvider(gcsProjectId, gcsBucketName, gcsCredentialsPath);
    }
}
