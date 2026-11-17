package com.floras.cafe.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.floras.cafe.model.Role;
import com.floras.cafe.model.User;
import com.floras.cafe.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Register a new customer
    @Transactional
    public User signupCustomer(
            String username,
            String name,
            String email,
            String password) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Phone number is already registered");
        }

        if (email != null && !email.isBlank()
                && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        User user = new User();

        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // Never accept CUSTOMER/ADMIN role from frontend
        user.setRole(Role.CUSTOMER);

        return userRepository.save(user);
    }

    // Find user by phone number
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(
                        "User not found"));
    }

    // Get all customers (Admin)
    @Transactional(readOnly = true)
    public List<User> getAllCustomers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == Role.CUSTOMER)
                .toList();
    }

    // Find customer by ID
    @Transactional(readOnly = true)
    public User getCustomerById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Customer not found"));

        if (user.getRole() != Role.CUSTOMER) {
            throw new RuntimeException("Customer not found");
        }

        return user;
    }
}
