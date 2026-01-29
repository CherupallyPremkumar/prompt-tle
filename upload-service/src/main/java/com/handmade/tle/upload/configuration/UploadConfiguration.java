package com.handmade.tle.upload.configuration;

import com.handmade.tle.shared.repository.UploadRepository;
import com.handmade.tle.storage.StorageProvider;
import com.handmade.tle.upload.event.UploadEventPublisher;
import com.handmade.tle.upload.service.UploadOrchestrationService;
import com.handmade.tle.upload.service.ValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@Configuration
public class UploadConfiguration {

    @Value("${upload.max-file-size:10485760}")
    private long maxFileSize;

    @Value("${upload.allowed-types:image/jpeg,image/png,application/pdf}")
    private List<String> allowedFileTypes;

    @Value("${upload.presigned-expiration-seconds:3600}")
    private int presignedExpirationSeconds;

    @Bean
    public ValidationService uploadValidationService() {
        return new ValidationService(maxFileSize, allowedFileTypes);
    }

    @Bean
    public UploadEventPublisher uploadEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        return new UploadEventPublisher(kafkaTemplate);
    }

    @Bean
    public UploadOrchestrationService uploadOrchestrationService(
            StorageProvider storageProvider,
            UploadRepository uploadRepository,
            UploadEventPublisher uploadEventPublisher,
            ValidationService validationService) {
        return new UploadOrchestrationService(
                storageProvider,
                uploadRepository,
                uploadEventPublisher,
                validationService,
                presignedExpirationSeconds);
    }
}