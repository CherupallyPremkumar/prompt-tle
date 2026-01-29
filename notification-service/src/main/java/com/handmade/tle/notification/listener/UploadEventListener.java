package com.handmade.tle.notification.listener;

import com.handmade.tle.shared.dto.UploadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Notification Service Event Listener
 * Reacts to upload events to send notifications.
 */
@Slf4j
public class UploadEventListener {

    @KafkaListener(topics = "upload-completed", groupId = "notification-group")
    public void handleUploadCompleted(UploadEvent event) {
        log.info("📧 Notification Service: Received upload completed event for file: {}", event.getFilename());
        log.info("Sending confirmation email to user: {} for upload: {}", event.getUserId(), event.getUploadId());

        // Placeholder for real email/SMS/webhook logic
        sendEmail(event.getUserId(), event.getFilename());
    }

    private void sendEmail(String userId, String filename) {
        log.info("Sending email... [SUCCESS]");
    }
}
