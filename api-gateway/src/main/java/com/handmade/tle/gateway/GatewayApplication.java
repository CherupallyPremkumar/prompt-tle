package com.handmade.tle.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication(scanBasePackages = { "com.handmade.tle", "org.chenile.configuration" })
@EnableJpaRepositories(basePackages = { "com.handmade.tle.shared.repository" })
@EntityScan(basePackages = { "com.handmade.tle.shared.model" })
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.boot.ApplicationRunner runner(
            org.springframework.kafka.core.KafkaTemplate<?, ?> kafkaTemplate) {
        return args -> {
            System.out.println("KafkaTemplate is loaded: " + kafkaTemplate);
        };
    }
}
