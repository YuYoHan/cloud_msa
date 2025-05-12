package com.example.catalogsservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

// Spring이 Kafka 관련 Bean을 스캔하고 등록하도록 활성화함.
@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> properties = new HashMap<>();
        // Kafka container host
        // Kafka 브로커 주소 (kafka:9092). Kafka 컨테이너 이름이 kafka이고 같은 Docker 네트워크에 있어야 동작
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        // Consumer Group ID (consumerGroupId). Kafka에서 동일한 그룹 ID를 가진 컨슈머는 메시지를 분산받음
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroupId");
        // 메시지 키를 역직렬화할 클래스
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 메시지 값을 역직렬화할 클래스
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    // Kafka 메시지를 비동기적으로 처리하는 리스너 컨테이너를 만들어주는 Factory 설정
    // 내부적으로 여러 스레드를 사용해 병렬로 Kafka 메시지를 소비할 수 있음
    // @KafkaListener를 사용할 때 자동으로 이 factory를 기반으로 메시지를 처리
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory
                = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        return kafkaListenerContainerFactory;
    }


}
