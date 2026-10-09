package com.ait.app.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.OrderDto;

public interface OrderService {

	public ResponseEntity placeOrder(OrderDto orderDto);

	public ResponseEntity viewOrder(int orderId);
	
	public ResponseEntity getUserOrderHistory(int userId, Pageable pageable);
}