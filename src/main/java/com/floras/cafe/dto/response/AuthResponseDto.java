package com.floras.cafe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponseDto {

    private boolean success;
    private String message;
    private String token;
    private UserResponseDto user;
}