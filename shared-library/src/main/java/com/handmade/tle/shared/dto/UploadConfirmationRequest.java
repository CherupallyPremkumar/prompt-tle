package com.handmade.tle.shared.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadConfirmationRequest {
    @NotBlank(message = "Upload ID is required")
    private String uploadId;

    @NotBlank(message = "File key is required")
    private String fileKey;
}
