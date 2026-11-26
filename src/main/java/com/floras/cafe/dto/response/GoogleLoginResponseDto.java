package com.floras.cafe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleLoginResponseDto {

    private boolean success;

    private String message;

    private boolean requiresMobileNumber;

    private boolean requiresExistingLogin;

    private String token;

    private UserResponseDto user;
}