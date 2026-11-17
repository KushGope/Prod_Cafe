package com.floras.cafe.service;

import com.floras.cafe.model.MenuItem;
import com.floras.cafe.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

        private final MenuRepository menuRepository;

        public List<MenuItem> getAllMenuItems() {
                return menuRepository.findAll();
        }

        public MenuItem getProductById(Long id) {
                return menuRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Menu item not found: " + id));
        }

        public List<MenuItem> getAvailableMenuItems() {
                return menuRepository.findByAvailableTrue();
        }

        // public MenuItem getProductById(Long id) {
        // return dummyProducts.stream()
        // .filter(p -> p.getId().equals(id))
        // .findFirst()
        // .orElseThrow(() -> new RuntimeException("Menu item not found with id: " +
        // id));
        // }

}