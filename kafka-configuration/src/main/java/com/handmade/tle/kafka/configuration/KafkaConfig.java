package com.handmade.tle.kafka.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import com.handmade.tle.kafka.loader.KafkaCertificateLoader;
import com.handmade.tle.kafka.loader.ProductionCertificateLoader;

@Configuration
@EnableKafka
public class KafkaConfig {

    @org.springframework.beans.factory.annotation.Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @org.springframework.beans.factory.annotation.Value("${spring.kafka.consumer.group-id:api-gateway-group}")
    private String groupId;

    @org.springframework.beans.factory.annotation.Value("${spring.ssl.bundle.jks.kafka.keystore.password:}")
    private String keystorePassword;

    @org.springframework.beans.factory.annotation.Value("${spring.ssl.bundle.jks.kafka.truststore.password:}")
    private String truststorePassword;

    @org.springframework.beans.factory.annotation.Value("${spring.kafka.ssl.keystore-content:}")
    private String keystoreContent;

    @org.springframework.beans.factory.annotation.Value("${spring.kafka.ssl.truststore-content:}")
    private String truststoreContent;

    private final KafkaCertificateLoader certificateLoader;

    public KafkaConfig(KafkaCertificateLoader certificateLoader) {
        this.certificateLoader = certificateLoader;
    }

    @Bean
    public static KafkaCertificateLoader certificateLoader() {
        return new ProductionCertificateLoader();
    }

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
    @Primary
    public ProducerFactory<String, Object> sslProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringSerializer.class);

        configureSsl(props);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    @Primary
    public KafkaTemplate<String, Object> sslKafkaTemplate(ProducerFactory<String, Object> producerFactory,
                                                     RecordMessageConverter messageConverter) {
        KafkaTemplate<String, Object> template = new KafkaTemplate<>(producerFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    @Primary
    public ConsumerFactory<String, Object> sslConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                org.apache.kafka.common.serialization.StringDeserializer.class);

        configureSsl(props);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    @Primary
    public ConcurrentKafkaListenerContainerFactory<String, Object> sslKafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            RecordMessageConverter messageConverter) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordMessageConverter(messageConverter);
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
