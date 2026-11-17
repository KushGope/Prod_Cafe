package com.floras.cafe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.floras.cafe.model.ShopSettings;
import com.floras.cafe.service.ShopService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/status")
    public ResponseEntity<ShopSettings> getStatus() {
        return ResponseEntity.ok(shopService.getStatus());
    }
}