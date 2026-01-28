package com.handmade.tle.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadEvent {
    private String uploadId;
    private String userId;
    private String filename;
    private String fileKey;
    private Long fileSize;
    private Instant completedAt;
}
