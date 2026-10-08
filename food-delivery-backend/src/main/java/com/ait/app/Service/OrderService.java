package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.OrderDto;
import com.ait.app.requestBody.OrderStatusUpdateDto;

public interface OrderService {

	ResponseEntity placeOrder(OrderDto orderDto);

	ResponseEntity viewOrder(int orderId);

	ResponseEntity updateOrderStatus(int orderId, OrderStatusUpdateDto statusUpdate);
}
