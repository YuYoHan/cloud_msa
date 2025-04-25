package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.dto.RequestOrder;
import com.example.orderservice.dto.ResponseOrder;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {
    private final Environment env;
    private final OrderService orderService;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's working in User Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<?> createOrder(@RequestBody RequestOrder order,
                                         @PathVariable("userId")String userId) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderDTO orderDTO = mapper.map(order, OrderDTO.class);
        orderDTO.setUserId(userId);
        ResponseOrder responseOrder = orderService.createdOrder(orderDTO);
        return ResponseEntity.ok().body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable("userId")String userId) {
        List<ResponseOrder> response = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok().body(response);
    }
}
