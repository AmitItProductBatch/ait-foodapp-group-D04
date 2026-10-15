package com.ait.app.event;

import com.ait.app.model.OrderStatus;

public class OrderStatusChangedEvent {

    private final int orderId;
    private final int customerId;
    private final OrderStatus previousStatus;
    private final OrderStatus newStatus;

    public OrderStatusChangedEvent(int orderId, int customerId, OrderStatus previousStatus,
            OrderStatus newStatus) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public OrderStatus getPreviousStatus() {
        return previousStatus;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }
}
