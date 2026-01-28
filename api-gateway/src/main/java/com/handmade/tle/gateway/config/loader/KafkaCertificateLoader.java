package com.handmade.tle.gateway.config.loader;

import java.io.IOException;

public interface KafkaCertificateLoader {
    String loadCertificate(String filename, String content) throws IOException;
}
