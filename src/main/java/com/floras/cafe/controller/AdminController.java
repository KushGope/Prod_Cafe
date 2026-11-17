
package com.floras.cafe.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.floras.cafe.dto.request.UpdateOrderDto;
import com.floras.cafe.dto.response.OrderResponseDto;
import com.floras.cafe.dto.response.UserResponseDto;
import com.floras.cafe.model.User;
import com.floras.cafe.service.OrderService;
import com.floras.cafe.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

        private final UserService userService;
        private final OrderService orderService;

        // Get all customers
        @GetMapping("/customers")
        public ResponseEntity<List<UserResponseDto>> getAllCustomers() {

                List<UserResponseDto> customers = userService.getAllCustomers()
                                .stream()
                                .map(this::mapToResponse)
                                .toList();

                return ResponseEntity.ok(customers);
        }

        // Get all orders
        @GetMapping("/orders")
        public ResponseEntity<List<OrderResponseDto>> getAllOrders() {

                return ResponseEntity.ok(
                                orderService.getAllOrders());
        }

        @PatchMapping("/orders/{orderId}/status")
        public ResponseEntity<Void> updateOrder(
                        @PathVariable String orderId,
                        @RequestBody UpdateOrderDto request) {

                orderService.updateOrder(
                                orderId,
                                request.getStatus());

                return ResponseEntity.noContent().build();
        }

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

}
