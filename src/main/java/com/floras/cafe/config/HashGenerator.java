package com.floras.cafe.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class HashGenerator {

    @Bean
    CommandLineRunner generateHash(PasswordEncoder passwordEncoder) {
        return args -> {
            String rawPassword = "Flora@2026"; // Choose your admin password
            String encodedHash = passwordEncoder.encode(rawPassword);
            System.out.println("--- BCRYPT HASH ---");
            System.out.println(encodedHash);
            System.out.println("-------------------");
        };
    }
}