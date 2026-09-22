package com.ait.app.Service;

import com.ait.app.model.Cart;
import com.ait.app.requestBody.CartRequestDto;

public interface CartService {
	void saveCart(CartRequestDto request);
	public Cart createCart(int userId);
	void deleteFromCart(int cid);
}
