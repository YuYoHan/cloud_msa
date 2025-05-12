package com.example.catalogsservice.config;

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

    @KafkaListener(topics = "example-order-topic")
    public void proccessMessage(String kafkaMessage) {
        log.debug("Kafka Message =====> " + kafkaMessage);

        Map<Service, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        CalalogEntity calalogEntity = catalogRepository.findByProductId((String) map.get("productId"));
        calalogEntity.setStock(calalogEntity.getStock() - (Integer) map.get("qty"));
        catalogRepository.save(calalogEntity);
    }
}
