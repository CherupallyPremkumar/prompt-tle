package com.handmade.tle.gateway.config.loader;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Component
@Profile("prod")
public class ProductionCertificateLoader implements KafkaCertificateLoader {

    @Override
    public String loadCertificate(String filename, String content) throws IOException {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException(
                    "Base64 content for " + filename + " must be provided in 'prod' profile");
        }
        Path tempFile = Files.createTempFile(filename, ".tmp");
        Files.write(tempFile, Base64.getDecoder().decode(content));
        tempFile.toFile().deleteOnExit();
        return tempFile.toAbsolutePath().toString();
    }
}
