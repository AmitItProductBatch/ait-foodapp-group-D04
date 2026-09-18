package com.ait.app.Service;

import com.ait.app.model.CartItems;
import com.ait.app.requestBody.CartItemDto;

public interface CartItemService {
	CartItems createCartItem(CartItemDto dto);
	void deleteCartItem(int id);
}
