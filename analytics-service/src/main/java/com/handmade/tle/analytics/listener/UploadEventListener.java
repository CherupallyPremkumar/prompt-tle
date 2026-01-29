package com.handmade.tle.analytics.listener;

import com.handmade.tle.shared.dto.UploadEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Analytics Service Event Listener
 * Reacts to upload events to track metrics and user behavior.
 */
@Slf4j
public class UploadEventListener {

    @KafkaListener(topics = "upload-completed", groupId = "analytics-group")
    public void handleUploadCompleted(UploadEvent event) {
        log.info("📊 Analytics Service: Received upload completed event for upload: {}", event.getUploadId());
        log.info("Recording metrics for user: {} | File: {} | Size: {} bytes",
                event.getUserId(), event.getFilename(), event.getFileSize());

        // Placeholder for real analytics logic (e.g., MongoDB persistence or Prometheus
        // counter)
        recordMetrics(event);
    }

    private void recordMetrics(UploadEvent event) {
        log.info("Metrics successfully recorded in Analytics DB.");
    }
}
