package com.ait.app.requestBody;

import com.ait.app.model.OrderStatus;

/** Request used by a restaurant owner or platform administrator to advance an order. */
public class OrderStatusUpdateDto {
    private OrderStatus status;
    private int userId;

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
