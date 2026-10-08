package com.ait.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ait.app.Service.OrderService;
import com.ait.app.requestBody.OrderDto;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	OrderService orderService;

	@PostMapping
	public ResponseEntity<?> placeOrder(@RequestBody OrderDto orderDto) {

		return orderService.placeOrder(orderDto);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<?> viewOrder(@PathVariable int orderId) {

		return orderService.viewOrder(orderId);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity getUserOrderHistory(@PathVariable int userId,Pageable pageable) {

	    return orderService.getUserOrderHistory(userId,pageable);
	}
}