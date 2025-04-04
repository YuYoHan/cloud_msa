package com.example.userservice.dto;

import lombok.*;

import java.util.Date;

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

    public void setUserId(String randomUUID) {
        this.userId = randomUUID;
    }
}
