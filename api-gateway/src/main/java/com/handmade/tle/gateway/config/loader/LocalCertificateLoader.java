package com.handmade.tle.gateway.config.loader;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
@Profile("!prod")
public class LocalCertificateLoader implements KafkaCertificateLoader {

    @Override
    public String loadCertificate(String filename, String content) throws IOException {
        // In local/dev, we ignore the 'content' (Base64) and look for the file in
        // classpath
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kafka/" + filename)) {
            if (inputStream == null) {
                // Return original local path if it exists for backward compat during dev
                File localFile = new File(
                        "/Users/premkumar/Documents/TokenLimitExceeded/api-gateway/src/main/resources/kafka/"
                                + filename);
                if (localFile.exists()) {
                    return localFile.getAbsolutePath();
                }
                throw new FileNotFoundException("Certificate not found in classpath or filesystem: kafka/" + filename);
            }
            Path tempFile = Files.createTempFile(filename, ".tmp");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            tempFile.toFile().deleteOnExit();
            return tempFile.toAbsolutePath().toString();
        }
    }
}
