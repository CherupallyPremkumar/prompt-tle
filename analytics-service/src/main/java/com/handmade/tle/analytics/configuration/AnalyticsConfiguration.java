package com.handmade.tle.analytics.configuration;

import com.handmade.tle.analytics.listener.UploadEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalyticsConfiguration {

    @Bean
    public UploadEventListener analyticsUploadEventListener() {
        return new UploadEventListener();
    }
}
