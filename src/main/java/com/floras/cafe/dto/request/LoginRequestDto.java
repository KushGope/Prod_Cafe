package com.floras.cafe.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {

    @NotBlank(message = "Username is required")
    private String username; // Phone number

    @NotBlank(message = "Password is required")
    private String password;

}