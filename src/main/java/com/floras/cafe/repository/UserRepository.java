package com.floras.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.floras.cafe.dto.response.UserResponseDto;
import com.floras.cafe.model.Role;
import com.floras.cafe.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByGoogleId(String googleId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
                SELECT new com.floras.cafe.dto.response.UserResponseDto(
                    u.id,
                    u.username,
                    u.name,
                    u.role,
                    u.createdAt,
                    COUNT(o.id)
                )
                FROM User u
                LEFT JOIN Order o ON o.user = u
                WHERE u.role = :role
                GROUP BY u.id, u.username, u.name, u.role, u.createdAt
                ORDER BY u.createdAt DESC
            """)
    List<UserResponseDto> findCustomersWithOrderCount(
            @Param("role") Role role);
}