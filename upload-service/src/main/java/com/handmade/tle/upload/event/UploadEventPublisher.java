package com.handmade.tle.upload.event;

import com.handmade.tle.shared.dto.UploadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j

public class UploadEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String UPLOAD_COMPLETED_TOPIC = "upload-completed";

    public UploadEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUploadCompleted(UploadEvent event) {
        log.info("Publishing upload completed event: {}", event.getUploadId());
        Message<UploadEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, UPLOAD_COMPLETED_TOPIC)
                .setHeader(KafkaHeaders.KEY, event.getUploadId())
                .build();
        kafkaTemplate.send(message);
    }
}