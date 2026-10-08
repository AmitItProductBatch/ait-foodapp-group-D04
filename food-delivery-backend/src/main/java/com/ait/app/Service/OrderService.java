package com.ait.app.Service;

import java.awt.print.Pageable;

import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.OrderDto;

public interface OrderService {

	ResponseEntity placeOrder(OrderDto orderDto);

	ResponseEntity viewOrder(int orderId);

	ResponseEntity getUserOrderHistory(int userId, org.springframework.data.domain.Pageable pageable);
}