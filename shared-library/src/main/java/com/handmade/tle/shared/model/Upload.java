package com.handmade.tle.shared.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "uploads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Upload {
    @Id
    private String uploadId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String fileKey;

    private String contentType;

    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UploadStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant completedAt;

    private Instant expiresAt;

    private String downloadUrl;
}
