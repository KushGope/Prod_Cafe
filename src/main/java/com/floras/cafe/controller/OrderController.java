package com.floras.cafe.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.floras.cafe.dto.request.OrderRequestDto;
import com.floras.cafe.dto.response.OrderResponseDto;
import com.floras.cafe.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Public or customer-accessible endpoint for order status tracking
    @GetMapping("/token/{orderToken}")
    public ResponseEntity<OrderResponseDto> getOrderByToken(
            @PathVariable Integer orderToken) {
        return ResponseEntity.ok(
                orderService.getOrderByToken(orderToken));
    }

    // Get logged-in customer's orders
    @GetMapping("/myorders")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                orderService.getOrdersByUsername(
                        userDetails.getUsername()));
    }

    // Place order for logged-in customer
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequest,
            Authentication authentication) {
        OrderResponseDto response = orderService.placeOrder(orderRequest, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}