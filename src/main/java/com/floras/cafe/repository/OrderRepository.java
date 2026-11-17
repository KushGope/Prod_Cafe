package com.floras.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import com.floras.cafe.model.Order;
import com.floras.cafe.model.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByOrderToken(Integer orderToken);

    @Query("""
                SELECT DISTINCT o
                FROM Order o
                LEFT JOIN FETCH o.items i
                LEFT JOIN FETCH i.menuItem
            """)

    List<Order> findAllWithItemsAndMenuItems();

    @Query("""
                SELECT DISTINCT o
                FROM Order o
                LEFT JOIN FETCH o.items i
                LEFT JOIN FETCH i.menuItem
                WHERE o.user.username = :username
                ORDER BY o.createdAt DESC
            """)

    List<Order> findOrdersWithItemsByUsername(
            @Param("username") String username);

    List<Order> findByUser_UsernameOrderByCreatedAtDesc(String username);

    List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);

    List<Order> findByStatusNotOrderByCreatedAtAsc(OrderStatus status);

    List<Order> findAllByOrderByCreatedAtDesc();
}
