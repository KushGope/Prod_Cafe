package com.floras.cafe.config;

import com.floras.cafe.model.MenuItem;
import com.floras.cafe.repository.MenuRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MenuDataInitializer implements CommandLineRunner {

        private final MenuRepository menuRepository;
        private final ObjectMapper objectMapper;

        @Override
        public void run(String... args) {

                if (menuRepository.count() > 1) {
                        System.out.println("Menu already exists. Skipping initialization.");
                        return;
                }
                ClassPathResource resource = new ClassPathResource("data/menu-items.json");
                try (InputStream inputStream = resource.getInputStream()) {

                        List<MenuItem> items = objectMapper.readValue(
                                        inputStream,
                                        new TypeReference<List<MenuItem>>() {
                                        });

                        menuRepository.saveAll(items);

                        log.info("{} menu items initialized successfully!",
                                        items.size());
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }

}