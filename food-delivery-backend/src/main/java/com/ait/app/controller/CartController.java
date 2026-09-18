package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.CartItemService;
import com.ait.app.Service.CartService;
import com.ait.app.requestBody.CartItemDto;
import com.ait.app.requestBody.CartResponseDto;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private CartService cartService;

	@PostMapping("/items")
	public ResponseEntity<String> saveCartItem(@RequestBody CartItemDto dto) {

		cartItemService.createCartItem(dto);

		return new ResponseEntity("Cart item added successfully", HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<CartResponseDto> getCart() {

		return new ResponseEntity<>(cartService.getCart(), HttpStatus.OK);
	}
}