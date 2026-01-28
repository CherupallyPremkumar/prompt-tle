package com.handmade.tle.shared.dto;

import com.handmade.tle.shared.model.UploadStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadConfirmationResponse {
    private String uploadId;
    private String fileKey;
    private String fileUrl;
    private String downloadUrl;
    private UploadStatus status;
    private Long fileSize;
    private Instant completedAt;
    private String message;
}
