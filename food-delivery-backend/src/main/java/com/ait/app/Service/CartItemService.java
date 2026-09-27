package com.ait.app.Service;

import com.ait.app.model.CartItems;
import com.ait.app.requestBody.CartItemDto;
import com.ait.app.requestBody.CartItemQuantityDto;
import com.ait.app.requestBody.CartResponseDto;

public interface CartItemService {
	CartItems createCartItem(CartItemDto dto);
	void deleteCartItem(int id);
	CartResponseDto updateCartItem(int id, CartItemQuantityDto dto);
}
