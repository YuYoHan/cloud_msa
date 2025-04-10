package com.example.catalogsservice.dto;

import lombok.Data;

@Data
public class CatalogDTO {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private String orderId;
    private String userId;
}
