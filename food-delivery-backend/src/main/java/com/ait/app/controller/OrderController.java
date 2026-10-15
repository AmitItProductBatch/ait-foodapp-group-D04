package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	@DeleteMapping("/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable int id) {

		orderService.cancelOrder(id);

		return new ResponseEntity<>("Order cancelled successfully", HttpStatus.OK);
	}
}
