package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.dto.ResponseOrder;

import java.util.List;

public interface OrderService {
    ResponseOrder createdOrder(OrderDTO orderDetails);
    List<ResponseOrder> getOrdersByUserId(String userId);
    OrderDTO getOrderByOrderId(String orderId);
}
