package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.OrderDto;

public interface OrderService {

	ResponseEntity placeOrder(OrderDto orderDto);

	ResponseEntity viewOrder(int orderId);
}