package com.example.orderservice.dto.kafka;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
public class KafkaOrderDTO implements Serializable {
    private Schema schema;
    private Payload payload;
}
