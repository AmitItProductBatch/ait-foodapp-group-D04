package com.ait.app.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.Service.CartService;
import com.ait.app.customExceptionHandler.CartException;
import com.ait.app.model.Cart;
import com.ait.app.model.CartItems;
import com.ait.app.model.User;
import com.ait.app.repository.CartItemRepository;
import com.ait.app.repository.CartRepository;
import com.ait.app.repository.UserRepository;
import com.ait.app.requestBody.CartItemResponseDto;
import com.ait.app.requestBody.CartResponseDto;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	@Override
	public Cart createCart(int userId) {

		Optional<User> optionalUser = userRepository.findById(userId);

		if (optionalUser.isEmpty()) {
			throw new CartException("Cart creation failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		User user = optionalUser.get();

		Cart cart = new Cart();
		cart.setUser(user);

		return cartRepository.save(cart);
	}

	@Override
	public CartResponseDto getCart() {

		int userId = 8;

		Optional<Cart> optionalCart = cartRepository.findByUserId(userId);

		if (optionalCart.isEmpty()) {
			throw new CartException("Cart not found", HttpStatus.NOT_FOUND);
		}

		Cart cart = optionalCart.get();

		List<CartItems> cartItems = cartItemRepository.findByCartId(cart.getCartid());

		List<CartItemResponseDto> itemResponseList = new ArrayList<>();

		double totalAmount = 0.0;
		int restaurantId = 0;

		for (CartItems cartItem : cartItems) {

			CartItemResponseDto itemResponse = new CartItemResponseDto();

			itemResponse.setItemId(cartItem.getMenuItem().getId());
			itemResponse.setItemName(cartItem.getMenuItem().getName());
			itemResponse.setUnitPrice(cartItem.getUnitprice());
			itemResponse.setQuantity(cartItem.getQuantity());
			itemResponse.setSubtotal(cartItem.getSubtotal());

			itemResponseList.add(itemResponse);

			totalAmount = totalAmount + cartItem.getSubtotal();

			if (restaurantId == 0) {
				restaurantId = cartItem.getMenuItem().getRestaurant().getId();
			}
		}

		CartResponseDto response = new CartResponseDto();

		response.setCartId(cart.getCartid());
		response.setRestaurantId(restaurantId);
		response.setItems(itemResponseList);
		response.setTotalAmount(totalAmount);

		return response;
	}

}
