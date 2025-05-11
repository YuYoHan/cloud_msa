package com.example.userservice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class UserDTO {
    private String email;
    private String pw;
    private String name;
    private String userId;
    private Date createAt;
    private String encryptedPw;
    @Builder.Default
    private List<ResponseOrder> orders = new ArrayList<>();

    public void setUserId(String randomUUID) {
        this.userId = randomUUID;
    }

    public void setOrders(List<ResponseOrder> orders) {
        this.orders.addAll(orders);
    }
}
