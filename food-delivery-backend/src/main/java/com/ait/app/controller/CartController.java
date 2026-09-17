package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.CartItemService;
import com.ait.app.requestBody.CartItemDto;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	@Autowired
	private CartItemService cartItemService;

	@PostMapping("/items")
	public ResponseEntity<String> saveCartItem(@RequestBody CartItemDto dto) {

		cartItemService.createCartItem(dto);

		return new ResponseEntity("Cart item added successfully", HttpStatus.CREATED);
	}
}
