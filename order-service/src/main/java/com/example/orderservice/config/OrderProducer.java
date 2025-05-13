package com.example.orderservice.config;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.dto.kafka.Field;
import com.example.orderservice.dto.kafka.KafkaOrderDTO;
import com.example.orderservice.dto.kafka.Payload;
import com.example.orderservice.dto.kafka.Schema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fields = Arrays.asList(
            Field.builder()
                    .type("string")
                    .optional(true)
                    .field("orderId")
                    .build(),
            Field.builder()
                    .type("string")
                    .optional(true)
                    .field("userId")
                    .build(),
            Field.builder()
                    .type("string")
                    .optional(true)
                    .field("productId")
                    .build(),
            Field.builder()
                    .type("int32")
                    .optional(true)
                    .field("qty")
                    .build(),
            Field.builder()
                    .type("int32")
                    .optional(true)
                    .field("unitPrice")
                    .build(),
            Field.builder()
                    .type("int32")
                    .optional(true)
                    .field("totalPrice")
                    .build()
            );

    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("orders")
            .build();

    public OrderDTO send(String kafkaTopic, OrderDTO orderDTO) {
        Payload payload = Payload.builder()
                .orderId(orderDTO.getOrderId())
                .userId(orderDTO.getUserId())
                .productId(orderDTO.getProductId())
                .qty(orderDTO.getQty())
                .unitPrice(orderDTO.getUnitPrice())
                .totalPrice(orderDTO.getTotalPrice())
                .build();

        KafkaOrderDTO kafkaOrderDTO = KafkaOrderDTO.builder()
                .schema(schema)
                .payload(payload)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaOrderDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(kafkaTopic, jsonInString);
        log.debug("Order Producer send data for the Order microservice : " + kafkaOrderDTO);
        return orderDTO;
    }
}
