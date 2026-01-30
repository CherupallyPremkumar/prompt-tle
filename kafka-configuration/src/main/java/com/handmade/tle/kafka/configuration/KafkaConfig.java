package com.handmade.tle.kafka.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import com.handmade.tle.shared.dto.UploadEvent;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import com.handmade.tle.kafka.loader.KafkaCertificateLoader;
import com.handmade.tle.kafka.loader.ProductionCertificateLoader;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;


@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:api-gateway-group}")
    private String groupId;

    @Value("${spring.ssl.bundle.jks.kafka.keystore.password:}")
    private String keystorePassword;

    @Value("${spring.ssl.bundle.jks.kafka.truststore.password:}")
    private String truststorePassword;

    @Value("${spring.kafka.ssl.keystore-content:}")
    private String keystoreContent;

    @Value("${spring.kafka.ssl.truststore-content:}")
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
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        configureSsl(props); // your SSL config
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    @Primary
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    @Primary
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        configureSsl(props);

        // ✅ Use JacksonJsonDeserializer instead of JsonDeserializer
        JacksonJsonDeserializer<Object> jacksonDeserializer = new JacksonJsonDeserializer<>(Object.class);
        jacksonDeserializer.addTrustedPackages("*");
        jacksonDeserializer.setUseTypeHeaders(false);

        ErrorHandlingDeserializer<Object> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(jacksonDeserializer);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                errorHandlingDeserializer
        );
    }

    @Bean
    @Primary
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, UploadEvent> uploadEventConsumerFactory() {
        ensureCertificatesLoaded();

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId); // Default group, listeners can override

        configureSsl(props);

        JacksonJsonDeserializer<UploadEvent> deserializer = new JacksonJsonDeserializer<>(UploadEvent.class);
        deserializer.addTrustedPackages("com.handmade.tle.shared.dto");
        deserializer.setUseTypeHeaders(false);

        ErrorHandlingDeserializer<UploadEvent> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(deserializer);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                errorHandlingDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UploadEvent> uploadEventKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UploadEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(uploadEventConsumerFactory());
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
