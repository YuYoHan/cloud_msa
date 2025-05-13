package com.example.orderservice.dto.kafka;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
public class Field {
    private String type;
    private boolean optional;
    private String field;
}
