package com.example.orderservice.controller;

import com.example.orderservice.config.KafkaProducer;
import com.example.orderservice.config.OrderProducer;
import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.dto.RequestOrder;
import com.example.orderservice.dto.ResponseOrder;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
@Slf4j
public class OrderController {
    private final Environment env;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

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

        // JPA 처리
//        ResponseOrder responseOrder = orderService.createdOrder(orderDTO);

        // 카프카 처리
        orderDTO.setOrderId(UUID.randomUUID().toString());
        orderDTO.setTotalPrice(order.getQty() * order.getUnitPrice());


        // 주문 데이터를 Kafka에 보내기
        kafkaProducer.send("example-order-topic", orderDTO);
        orderProducer.send("orders", orderDTO);

        ResponseOrder responseOrder = mapper.map(orderDTO, ResponseOrder.class);

        return ResponseEntity.ok().body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<?> getOrders(@PathVariable("userId")String userId) {
        log.debug("Before call orders microservice");
        List<ResponseOrder> response = orderService.getOrdersByUserId(userId);
        log.debug("After called orders microservice");
        return ResponseEntity.ok().body(response);
    }
}
