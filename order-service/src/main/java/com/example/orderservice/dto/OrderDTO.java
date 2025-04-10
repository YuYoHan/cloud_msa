package com.example.orderservice.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@Setter
public class OrderDTO {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private String orderId;
    private String userId;
}
