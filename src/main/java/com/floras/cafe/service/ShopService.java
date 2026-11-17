package com.floras.cafe.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.floras.cafe.model.ShopSettings;
import com.floras.cafe.repository.ShopRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository repository;

    // GET STATUS
    public ShopSettings getStatus() {
        return repository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Shop settings not found"));
    }

    // OPEN / CLOSE
    @Transactional
    public ShopSettings updateStatus(boolean open) {

        ShopSettings shop = getStatus();
        shop.setOpen(open);
        return repository.save(shop);
    }

    // CHECK BEFORE ORDER
    public void checkShopOpen() {

        if (!getStatus().isOpen()) {
            throw new RuntimeException(
                    "Shop is currently closed. Orders are unavailable.");
        }
    }
}