package com.floras.cafe.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class OrderRequestDto {
    private List<OrderItemDto> items;

    @Data
    public static class OrderItemDto {
        private Long id; // Product ID from frontend
        private int quantity;
    }

}