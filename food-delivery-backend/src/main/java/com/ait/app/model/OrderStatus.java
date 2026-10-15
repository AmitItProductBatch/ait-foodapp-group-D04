package com.ait.app.model;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/** The lifecycle states an order may move through. */
public enum OrderStatus {
    PLACED,
    CONFIRMED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED;

    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS = Map.of(
            PLACED, EnumSet.of(CONFIRMED),
            CONFIRMED, EnumSet.of(PREPARING),
            PREPARING, EnumSet.of(OUT_FOR_DELIVERY),
            OUT_FOR_DELIVERY, EnumSet.of(DELIVERED),
            DELIVERED, EnumSet.noneOf(OrderStatus.class));

    public boolean canTransitionTo(OrderStatus target) {
        return target != null && VALID_TRANSITIONS.getOrDefault(this, Set.of()).contains(target);
    }
}
