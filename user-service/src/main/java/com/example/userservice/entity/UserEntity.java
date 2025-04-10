package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Table(name = "users")
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50, unique = true)
    private String email;
    @Column(nullable = false, length = 50)
    private String userId;
    @Column(nullable = false, unique = true)
    private String encryptedPw;

    public void setEncryptedPw(String encryptedPassword) {
        this.encryptedPw = encryptedPassword;
    }
}
