package com.floras.cafe.service;

import java.util.List;

import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.floras.cafe.dto.response.UserResponseDto;
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

    // Find user by google
    @Transactional(readOnly = true)
    public User getUserByGoogleId(String googleId) {
        return userRepository.findByGoogleId(googleId)
                .orElse(null);
    }// Check whether mobile number exists

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Create Google customer
    @Transactional
    public User signupGoogleCustomer(
            String mobileNumber,
            String name,
            String email,
            String googleId) {

        if (userRepository.existsByUsername(mobileNumber)) {
            throw new IllegalArgumentException(
                    "Mobile number is already registered");
        }

        if (userRepository.findByGoogleId(googleId).isPresent()) {
            throw new IllegalArgumentException(
                    "Google account is already registered");
        }

        User user = new User();

        user.setUsername(mobileNumber);
        user.setName(name);
        user.setEmail(email);
        user.setGoogleId(googleId);

        // OAuth users do not authenticate using this password.
        user.setPassword(passwordEncoder.encode(
                UUID.randomUUID().toString()));

        user.setRole(Role.CUSTOMER);

        return userRepository.save(user);
    }

    @Transactional
    public User linkGoogleAccount(
            String username,
            String googleId,
            String googleEmail) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found"));

        if (user.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException(
                    "Only customers can link Google accounts");
        }

        if (user.getGoogleId() != null
                && !user.getGoogleId().equals(googleId)) {
            throw new IllegalArgumentException(
                    "Another Google account is already linked");
        }

        User existingGoogleUser = getUserByGoogleId(googleId);

        if (existingGoogleUser != null
                && !existingGoogleUser.getId().equals(user.getId())) {
            throw new IllegalArgumentException(
                    "This Google account is already linked to another customer");
        }

        user.setGoogleId(googleId);

        return userRepository.save(user);
    }

    // Get all customers with order count (Admin)
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllCustomers() {

        return userRepository.findCustomersWithOrderCount(Role.CUSTOMER);
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
