package com.floras.cafe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import org.springframework.transaction.annotation.Transactional;

import com.floras.cafe.dto.request.OrderRequestDto;
import com.floras.cafe.dto.request.OrderRequestDto.OrderItemDto;
import com.floras.cafe.dto.response.OrderResponseDto;
import com.floras.cafe.dto.response.OrderResponseDto.OrderItemResponseDto;
import com.floras.cafe.model.MenuItem;
import com.floras.cafe.model.Order;
import com.floras.cafe.model.OrderItem;
import com.floras.cafe.model.OrderStatus;
import com.floras.cafe.model.User;
import com.floras.cafe.repository.MenuRepository;
import com.floras.cafe.repository.OrderRepository;
import com.floras.cafe.repository.UserRepository;

import jakarta.persistence.EntityManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

        private final ShopService shopService;
        private final OrderRepository orderRepository;
        private final UserRepository userRepository;
        private final MenuRepository menuRepository;
        private final EntityManager entityManager;

        // private final SimpMessagingTemplate messagingTemplate;

        @Transactional(readOnly = true)
        public List<OrderResponseDto> getAllOrders() {
                List<Order> orders = orderRepository.findAllWithItemsAndMenuItems();
                return orders.stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        @Transactional
        public OrderResponseDto placeOrder(OrderRequestDto request, Authentication authentication) {

                // CHECK SHOP AVAILABILITY FIRST
                shopService.checkShopOpen();

                if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
                        throw new IllegalArgumentException("Order must contain items");
                }

                if (authentication == null ||
                                !authentication.isAuthenticated()) {

                        throw new IllegalArgumentException(
                                        "Authentication is required");
                }

                // Associate registered customer;
                User user = getCustomer(authentication);

                Order order = new Order(generateOrderToken());
                order.setUser(user);
                order.setStatus(OrderStatus.PENDING);
                // order.setId(UUID.randomUUID().toString());

                BigDecimal totalAmount = addOrderItems(
                                order,
                                request.getItems());

                log.info("========== ORDER DEBUG ==========");
                log.info("Order Token: {}", order.getOrderToken());
                log.info("Items count: {}", order.getItems().size());

                log.info("=================================");

                order.setTotalAmount(totalAmount);
                // Save Order in PostgreSQL
                Order savedOrder = orderRepository.save(order);
                // Order savedOrder = orderRepository.saveAndFlush(order);

                log.info("Saved Order ID: {}", savedOrder.getId());
                log.info("Saved Items Count: {}", savedOrder.getItems().size());

                log.info("Order placed successfully. Token: {}, Total: {}",
                                savedOrder.getOrderToken(),
                                savedOrder.getTotalAmount());

                return mapToResponse(savedOrder);

                // Broadcast mock notification to Admin dashboard
                // messagingTemplate.convertAndSend("/topic/admin", "New Test Order placed
                // successfully!");
        }

        @Transactional
        public void updateOrder(String orderId, OrderStatus newStatus) {

                if (newStatus == null) {
                        throw new IllegalArgumentException("Order status is required");
                }

                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

                OrderStatus currentStatus = order.getStatus();

                if (!currentStatus.canTransitionTo(newStatus)) {
                        throw new IllegalStateException(
                                        "Invalid order status transition: "
                                                        + currentStatus + " -> " + newStatus);
                }

                order.setStatus(newStatus);

                log.info("Order {} status changed from {} to {}",
                                order.getOrderToken(), currentStatus, newStatus);
        }

        @Transactional(readOnly = true)
        public List<OrderResponseDto> getOrdersByUsername(String username) {

                return orderRepository
                                .findOrdersWithItemsByUsername(username)
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public OrderResponseDto getOrderByToken(Integer orderToken) {
                Order order = orderRepository.findByOrderToken(orderToken)
                                .orElseThrow(() -> new RuntimeException(
                                                "Order not found with token: " + orderToken));
                return OrderResponseDto.builder()
                                .id(order.getId())
                                .orderToken(order.getOrderToken())
                                .status(order.getStatus().name())
                                .totalAmount(order.getTotalAmount())
                                .createdAt(order.getCreatedAt())
                                .build();
        }

        // HELPER METHODS

        // Generate unique order token using PostgreSQL
        private Integer generateOrderToken() {

                Number result = (Number) entityManager
                                .createNativeQuery("SELECT nextval('order_token_seq')")
                                .getSingleResult();
                return result.intValue();
                // TODO: ALTER SEQUENCE order_token_seq RESTART WITH 1001;
                // To reset the order number in db
        }

        private User getCustomer(Authentication authentication) {

                boolean isGuest = authentication.getAuthorities()
                                .stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_GUEST"));

                if (isGuest) {
                        return null;
                }

                boolean isCustomer = authentication.getAuthorities()
                                .stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));

                if (!isCustomer) {
                        throw new IllegalArgumentException(
                                        "Only customers and guests can place orders");
                }

                return userRepository.findByUsername(authentication.getName())
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        }

        private BigDecimal addOrderItems(Order order, List<OrderItemDto> items) {

                BigDecimal total = BigDecimal.ZERO;

                for (OrderItemDto item : items) {

                        if (item == null || item.getId() == null) {
                                throw new IllegalArgumentException("Menu item ID is required");
                        }

                        if (item.getQuantity() <= 0) {
                                throw new IllegalArgumentException(
                                                "Quantity must be greater than zero");
                        }

                        MenuItem product = menuRepository.findById(item.getId())
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "Product not found: " + item.getId()));

                        if (!product.isAvailable()) {
                                throw new IllegalArgumentException(
                                                "Product unavailable: " + product.getName());
                        }

                        BigDecimal price = product.getPrice();

                        BigDecimal subtotal = price.multiply(
                                        BigDecimal.valueOf(item.getQuantity()));

                        OrderItem orderItem = new OrderItem();
                        orderItem.setMenuItem(product);
                        orderItem.setQuantity(item.getQuantity());
                        orderItem.setPrice(price);
                        orderItem.setSubtotal(subtotal);

                        order.addItem(orderItem);

                        total = total.add(subtotal);
                }

                return total;
        }

        private OrderResponseDto mapToResponse(Order order) {

                List<OrderItemResponseDto> responseItems = order.getItems().stream()
                                .map(item -> OrderItemResponseDto.builder()
                                                .id(item.getMenuItem().getId())
                                                .name(item.getMenuItem().getName())
                                                .quantity(item.getQuantity())
                                                .price(item.getPrice())
                                                .subtotal(item.getSubtotal())
                                                .build())
                                .toList();

                User user = order.getUser();

                return OrderResponseDto.builder()
                                .id(order.getId())
                                .orderToken(order.getOrderToken())
                                .status(order.getStatus().name())
                                .totalAmount(order.getTotalAmount())
                                .createdAt(order.getCreatedAt())

                                // Customer details
                                .customerName(user != null ? user.getName() : "Guest")
                                .customerPhone(user != null ? user.getUsername() : null)

                                .items(responseItems)
                                .build();
        }

}
