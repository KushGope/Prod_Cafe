package com.floras.cafe.model;

import java.util.Set;

public enum OrderStatus {
    // PENDING,
    // CONFIRMED,
    // PREPARING,
    // READY,
    // COMPLETED,
    // CANCELLED

    COMPLETED(Set.of()),
    CANCELLED(Set.of()),
    READY(Set.of(COMPLETED)),
    PREPARING(Set.of(READY, CANCELLED)),
    CONFIRMED(Set.of(PREPARING, CANCELLED)),
    PENDING(Set.of(CONFIRMED, CANCELLED));

    private final Set<OrderStatus> allowedTransitions;

    OrderStatus(Set<OrderStatus> allowedTransitions) {
        this.allowedTransitions = allowedTransitions;
    }

    public boolean canTransitionTo(OrderStatus newStatus) {
        return allowedTransitions.contains(newStatus);
    }
}