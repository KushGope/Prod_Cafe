package com.floras.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.floras.cafe.model.ShopSettings;

public interface ShopRepository
        extends JpaRepository<ShopSettings, Long> {
}