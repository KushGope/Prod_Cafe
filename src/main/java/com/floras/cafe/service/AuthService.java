
package com.floras.cafe.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.UUID;

import com.floras.cafe.dto.request.LoginRequestDto;
import com.floras.cafe.dto.request.SignupRequestDto;
import com.floras.cafe.dto.response.AuthResponseDto;
import com.floras.cafe.dto.response.UserResponseDto;
import com.floras.cafe.model.Role;
import com.floras.cafe.model.User;
import com.floras.cafe.security.JwtService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class AuthService {

        private final UserService userService;
        private final JwtService jwtService;

        private final AuthenticationManager authenticationManager;

        // Customer registration
        @Transactional
        public AuthResponseDto signup(@Valid SignupRequestDto request) {

                User user = userService.signupCustomer(
                                request.getUsername(),
                                request.getName(),
                                request.getEmail(),
                                request.getPassword());

                return AuthResponseDto.builder()
                                .success(true)
                                .message("Registration successful")
                                .user(mapToResponse(user))
                                .build();
        }

        // Customer login
        @Transactional(readOnly = true)
        public AuthResponseDto login(@Valid LoginRequestDto request) {

                Authentication authentication = authenticate(request);
                validateRole(authentication, Role.CUSTOMER);

                // Find user by phone number
                User user = userService.getUserByUsername(
                                request.getUsername());

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                // Generate JWT
                String token = jwtService.generateToken(userDetails);

                return AuthResponseDto.builder()
                                .success(true)
                                .message("Login successful")
                                .token(token)
                                .user(mapToResponse(user))
                                .build();
        }

        // Guest login
        public AuthResponseDto guestLogin() {

                String guestId = "guest-" + UUID.randomUUID();

                UserDetails guestDetails = org.springframework.security.core.userdetails.User
                                .withUsername(guestId)
                                .password("")
                                .authorities("ROLE_GUEST")
                                .build();

                String token = jwtService.generateGuestToken(guestDetails);

                return AuthResponseDto.builder()
                                .success(true)
                                .message("Guest login successful")
                                .token(token)
                                .build();
        }

        // Admin login - in-memory authentication
        public AuthResponseDto adminLogin(@Valid LoginRequestDto request) {

                Authentication authentication = authenticate(request);
                validateRole(authentication, Role.ADMIN);

                User admin = userService.getUserByUsername(request.getUsername());

                // Get authenticated admin details
                UserDetails adminDetails = (UserDetails) authentication.getPrincipal();

                // Generate JWT containing ROLE_ADMIN
                String token = jwtService.generateToken(adminDetails);

                // Return response
                // UserResponseDto adminResponse = UserResponseDto.builder()
                // .username(adminDetails.getUsername())
                // .role(Role.ADMIN)
                // .build();

                return AuthResponseDto.builder()
                                .success(true)
                                .message("Admin login successful")
                                .token(token)
                                .user(mapToResponse(admin))
                                .build();
        }

        // Convert User entity to UserResponseDto
        private UserResponseDto mapToResponse(User user) {

                return UserResponseDto.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .name(user.getName())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .createdAt(user.getCreatedAt())
                                .build();
        }

        // Authenticate username and password
        private Authentication authenticate(LoginRequestDto request) {

                return authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));
        }

        private void validateRole(Authentication authentication, Role role) {

                boolean hasRole = authentication.getAuthorities()
                                .stream()
                                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role.name()));

                if (!hasRole) {
                        throw new IllegalArgumentException(
                                        "You are not authorized to access this portal");
                }
        }
}
