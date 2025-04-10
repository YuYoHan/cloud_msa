package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestLogin {
    @NotNull(message = "이메일은 null이면 안됩니다.")
    @Size(min = 2, message = "이메일은 2글자보다 아래이면 안됩니다.")
    @Email
    private String email;

    @NotNull(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8글자 이상을 써야합니다.")
    private String password;
}
