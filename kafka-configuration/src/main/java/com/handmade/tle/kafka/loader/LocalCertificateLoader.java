package com.handmade.tle.kafka.loader;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

public class LocalCertificateLoader implements KafkaCertificateLoader {

    @Override
    public String loadCertificate(String filename, String content) throws IOException {
        // If content is provided (e.g. from env vars), use it even in local profile
        if (content != null && !content.isBlank() && !"dummy".equalsIgnoreCase(content)) {
            Path tempFile = Files.createTempFile(filename, ".tmp");
            String sanitizedContent = content.replaceAll("\\s+", "");
            Files.write(tempFile, Base64.getDecoder().decode(sanitizedContent));
            tempFile.toFile().deleteOnExit();
            return tempFile.toAbsolutePath().toString();
        }

        // Fallback: look for the file in classpath
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kafka/" + filename)) {
            if (inputStream == null) {
                throw new FileNotFoundException(
                        "Certificate not found in provided content, classpath, or filesystem: kafka/" + filename);
            }
            Path tempFile = Files.createTempFile(filename, ".tmp");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            tempFile.toFile().deleteOnExit();
            return tempFile.toAbsolutePath().toString();
        }
    }
}
