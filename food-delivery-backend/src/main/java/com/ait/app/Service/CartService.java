package com.ait.app.Service;

import com.ait.app.model.Cart;
import com.ait.app.requestBody.CartResponseDto;

public interface CartService {

    public Cart createCart(int userId);

    public CartResponseDto getCart();

}
