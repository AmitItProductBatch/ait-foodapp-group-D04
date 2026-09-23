package com.ait.app.Service;

import com.ait.app.model.Cart;
import com.ait.app.requestBody.CartRequestDto;
import com.ait.app.requestBody.CartResponseDto;

public interface CartService {
	void saveCart(CartRequestDto request);
	public Cart createCart(int userId);
	public CartResponseDto getCart(int userId);
	void deleteFromCart(int cid);
	
}
