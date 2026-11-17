package com.floras.cafe.dto.request;

import com.floras.cafe.model.OrderStatus;

import lombok.Data;

@Data
public class UpdateOrderDto {
    private OrderStatus status;

}
