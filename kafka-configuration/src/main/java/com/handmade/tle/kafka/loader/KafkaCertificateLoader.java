package com.handmade.tle.kafka.loader;

import java.io.IOException;

public interface KafkaCertificateLoader {
    String loadCertificate(String filename, String content) throws IOException;
}
