package com.example.orderservice.dto.kafka;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
public class Payload {
    private String orderId;
    private String userId;
    private String productId;
    private int qty;
    private int unitPrice;
    private int totalPrice;
}
