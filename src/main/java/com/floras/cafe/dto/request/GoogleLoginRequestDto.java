
package com.floras.cafe.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequestDto {

    @NotBlank(message = "Google ID token is required")
    private String idToken;

    // Required only when registering a new customer
    private String mobileNumber;
}
