package com.handmade.tle.kafka.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ProductionCertificateLoader implements KafkaCertificateLoader {

    @Override
    public String loadCertificate(String filename, String content) throws IOException {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException(
                    "Base64 content for " + filename + " must be provided in 'prod' profile");
        }
        Path tempFile = Files.createTempFile(filename, ".tmp");
        String sanitizedContent = content.replaceAll("\\s+", "");
        Files.write(tempFile, Base64.getDecoder().decode(sanitizedContent));
        tempFile.toFile().deleteOnExit();
        return tempFile.toAbsolutePath().toString();
    }
}
