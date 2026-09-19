package com.ait.app.Service;

import com.ait.app.model.Cart;

public interface CartService {

	public Cart createCart(int userId);
	void deleteFromCart(int cid);
}
