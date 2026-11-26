
package com.floras.cafe.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.UUID;

import com.floras.cafe.dto.request.GoogleLoginRequestDto;
import com.floras.cafe.dto.request.LoginRequestDto;
import com.floras.cafe.dto.request.SignupRequestDto;
import com.floras.cafe.dto.response.AuthResponseDto;
import com.floras.cafe.dto.response.GoogleLoginResponseDto;
import com.floras.cafe.dto.response.UserResponseDto;
import com.floras.cafe.model.Role;
import com.floras.cafe.model.User;
import com.floras.cafe.security.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class AuthService {

        private final UserService userService;
        private final JwtService jwtService;

        private final AuthenticationManager authenticationManager;
        private final GoogleVerifierService googleVerifierService;

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

        // Google Login
        @Transactional
        public GoogleLoginResponseDto googleLogin(@Valid GoogleLoginRequestDto request) {

                // 1. Verify Google ID token
                GoogleIdToken.Payload payload = googleVerifierService.verify(request.getIdToken());

                // 2. Extract Google user information
                String googleId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                if (googleId == null || googleId.isBlank()) {
                        throw new IllegalArgumentException(
                                        "Invalid Google account");
                }

                // 3. Find existing user
                User user = userService.getUserByGoogleId(googleId);

                // 4. Register user if not already registered
                if (user != null) {
                        if (user.getRole() != Role.CUSTOMER) {
                                throw new IllegalArgumentException(
                                                "You are not authorized to access this portal");
                        }
                        return buildGoogleLoginResponse(user);
                }

                String username = request.getMobileNumber();

                if (username == null || username.isBlank()) {

                        return GoogleLoginResponseDto.builder()
                                        .success(true)
                                        .message("Please provide your mobile number")
                                        .requiresMobileNumber(true)
                                        .requiresExistingLogin(false)
                                        .build();
                }

                // 5. Check whether mobile number already exists
                if (userService.existsByUsername(username)) {

                        return GoogleLoginResponseDto.builder()
                                        .success(false)
                                        .message("This mobile number is already registered. Please login using your existing account to link Google.")
                                        .requiresMobileNumber(false)
                                        .requiresExistingLogin(true)
                                        .build();
                }

                // 6. Create new Google customer
                User newUser = userService.signupGoogleCustomer(
                                username,
                                name,
                                email,
                                googleId);

                // 7. Generate application JWT
                return buildGoogleLoginResponse(newUser);

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

        private GoogleLoginResponseDto buildGoogleLoginResponse(User user) {

                UserDetails userDetails = org.springframework.security.core.userdetails.User
                                .withUsername(user.getUsername())
                                .password(user.getPassword())
                                .authorities("ROLE_CUSTOMER")
                                .build();

                String token = jwtService.generateToken(userDetails);

                return GoogleLoginResponseDto.builder()
                                .success(true)
                                .message("Google login successful")
                                .requiresMobileNumber(false)
                                .requiresExistingLogin(false)
                                .token(token)
                                .user(mapToResponse(user))
                                .build();
        }
}
