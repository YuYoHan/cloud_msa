package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.dto.ResponseOrder;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;

    @Override
    public ResponseOrder createdOrder(OrderDTO order) {
        order.setOrderId(UUID.randomUUID().toString());
        order.setTotalPrice(order.getQty() * order.getUnitPrice());
        ModelMapper mapper = getModelMapper();
        OrderEntity orderEntity = mapper.map(order, OrderEntity.class);
        log.debug("오더 엔티티 {}", orderEntity);
        OrderEntity save = orderRepository.save(orderEntity);
        return mapper.map(save, ResponseOrder.class);
    }
    private static ModelMapper getModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }
    @Override
    public List<ResponseOrder> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId)
                .stream().map(order -> new ModelMapper().map(order, ResponseOrder.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        ModelMapper mapper = getModelMapper();
        OrderDTO response = mapper.map(orderEntity, OrderDTO.class);
        return response;
    }
}
