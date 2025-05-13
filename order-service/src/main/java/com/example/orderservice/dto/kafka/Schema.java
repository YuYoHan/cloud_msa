package com.example.orderservice.dto.kafka;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
public class Schema {
    private String type;
    private List<Field> fields;
    private boolean optional;
    private String name;
}
