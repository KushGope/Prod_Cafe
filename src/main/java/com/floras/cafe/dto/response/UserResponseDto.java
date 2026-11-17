
package com.floras.cafe.dto.response;

import java.time.LocalDateTime;

import com.floras.cafe.model.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private Long id;
    private String username;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
