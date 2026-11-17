
package com.floras.cafe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.floras.cafe.dto.request.LoginRequestDto;
import com.floras.cafe.dto.request.SignupRequestDto;
import com.floras.cafe.dto.response.AuthResponseDto;
import com.floras.cafe.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(
            @Valid @RequestBody SignupRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponseDto> adminLogin(
            @Valid @RequestBody LoginRequestDto request) {

        return ResponseEntity.ok(authService.adminLogin(request));
    }

    @PostMapping("/guest/login")
    public ResponseEntity<AuthResponseDto> guestLogin() {
        return ResponseEntity.ok(authService.guestLogin());
    }
}
