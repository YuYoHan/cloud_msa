package com.example.catalogsservice.messagequeue;

import com.example.catalogsservice.entity.CalalogEntity;
import com.example.catalogsservice.repository.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final CatalogRepository catalogRepository;

    @KafkaListener(topics = "example-catalog-topic")
    public void updateQty(String kafkaMessage) {
        log.debug("kafka message received: {}", kafkaMessage);

        Map<Object,Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        CalalogEntity entity = catalogRepository.findByProductId((String) map.get("productId"));
        if(entity != null) {
            entity.setStock(entity.getStock() - (Integer) map.get("qty"));
            catalogRepository.save(entity);
        }
    }
}
