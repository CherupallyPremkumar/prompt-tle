package com.handmade.tle.upload.event;

import com.handmade.tle.shared.dto.UploadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j

public class UploadEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String UPLOAD_COMPLETED_TOPIC = "upload-completed";

    public UploadEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUploadCompleted(UploadEvent event) {
        log.info("Publishing upload completed event: {}", event.getUploadId());
        kafkaTemplate.send(UPLOAD_COMPLETED_TOPIC, event.getUploadId(), event);
    }
}