package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ait.app.Service.OrderService;
import com.ait.app.requestBody.OrderDto;
import com.ait.app.requestBody.OrderStatusUpdateDto;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<?> placeOrder(@RequestBody OrderDto orderDto) {

		return orderService.placeOrder(orderDto);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<?> viewOrder(@PathVariable int orderId) {

		return orderService.viewOrder(orderId);
	}

	@PutMapping("/{orderId}/status")
	public ResponseEntity<?> updateOrderStatus(@PathVariable int orderId,
			@RequestBody OrderStatusUpdateDto statusUpdate) {
		return orderService.updateOrderStatus(orderId, statusUpdate);
	}
}
