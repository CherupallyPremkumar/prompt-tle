package com.handmade.tle.storage.configuration;

import com.handmade.tle.storage.StorageProvider;
import com.handmade.tle.storage.provider.S3StorageProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@Configuration
public class StorageConfiguration {


    @Bean
    public StorageProvider storageProvider() {
       return new S3StorageProvider();
    }
}
