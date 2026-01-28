package com.handmade.tle.gateway.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

@Configuration
@EnableKafka
public class KafkaConfig {

    @org.springframework.beans.factory.annotation.Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @org.springframework.beans.factory.annotation.Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @org.springframework.beans.factory.annotation.Value("${spring.ssl.bundle.jks.kafka.keystore.password}")
    private String keystorePassword;

    @org.springframework.beans.factory.annotation.Value("${spring.ssl.bundle.jks.kafka.truststore.password}")
    private String truststorePassword;

    @org.springframework.beans.factory.annotation.Value("${spring.kafka.ssl.keystore-content:}")
    private String keystoreContent;

    @org.springframework.beans.factory.annotation.Value("${spring.kafka.ssl.truststore-content:}")
    private String truststoreContent;

    @org.springframework.beans.factory.annotation.Autowired
    private com.handmade.tle.gateway.config.loader.KafkaCertificateLoader certificateLoader;

    private String keystoreLocation;
    private String truststoreLocation;

    private void ensureCertificatesLoaded() {
        if (this.keystoreLocation != null && this.truststoreLocation != null) {
            return; // Already loaded
        }
        try {
            this.keystoreLocation = certificateLoader.loadCertificate("client.keystore.p12", this.keystoreContent);
            this.truststoreLocation = certificateLoader.loadCertificate("client.truststore.jks",
                    this.truststoreContent);
            System.out.println("✅ Certificates loaded: " + keystoreLocation + ", " + truststoreLocation);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Kafka certificates", e);
        }
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                org.springframework.kafka.support.serializer.JsonSerializer.class);

        configureSsl(props);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                org.springframework.kafka.support.serializer.JsonDeserializer.class);
        props.put(org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES, "*");

        configureSsl(props);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    private void configureSsl(Map<String, Object> props) {
        ensureCertificatesLoaded();
        props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", truststoreLocation);
        props.put("ssl.truststore.password", truststorePassword);
        props.put("ssl.keystore.type", "PKCS12");
        props.put("ssl.keystore.location", keystoreLocation);
        props.put("ssl.keystore.password", keystorePassword);
        props.put("ssl.key.password", keystorePassword);
    }
}
