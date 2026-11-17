package com.floras.cafe.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponseDto {
    private String id;
    private Integer orderToken;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    // Customer details
    private String customerName;
    private String customerPhone;

    private List<OrderItemResponseDto> items;

    @Data
    @Builder
    public static class OrderItemResponseDto {

        private Long id;
        private String name;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal subtotal;
    }
}
