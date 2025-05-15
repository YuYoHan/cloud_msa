package com.example.userservice.client;

import com.example.userservice.dto.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {
    // Feign은 Gateway 없이 직접 호출하므로 /order-service/...가 그대로 필요합니다.
    @GetMapping("/order-service/{userId}orders")
    List<ResponseOrder> getOrders(@PathVariable String userId);
}
