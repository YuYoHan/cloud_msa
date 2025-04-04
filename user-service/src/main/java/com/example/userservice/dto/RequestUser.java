package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class RequestUser {
    @NotNull(message = "Email can not be null")
    @Size(min = 2, message = "Email not be less than two characters")
    @Email
    private String email;

    @NotNull(message = "Password can not be null")
    @Size(min = 8, message = "Password must be equal or grater than 8 characters and less than 16 characters")
    private String pw;

    @NotNull(message = "Name can not be null")
    @Size(min = 2, message = "이름은 2글자보다 적을 수 없습니다.")
    private String name;
}
