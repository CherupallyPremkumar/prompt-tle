package com.handmade.tle.notification.configuration;

import com.handmade.tle.notification.listener.UploadEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfiguration {

    @Bean
    public UploadEventListener notificationUploadEventListener() {
        return new UploadEventListener();
    }
}
