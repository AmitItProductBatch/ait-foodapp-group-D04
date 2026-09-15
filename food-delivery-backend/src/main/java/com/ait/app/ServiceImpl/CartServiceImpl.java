package com.ait.app.ServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.Service.CartService;
import com.ait.app.customExceptionHandler.CartException;
import com.ait.app.model.Cart;
import com.ait.app.model.User;
import com.ait.app.repository.CartRepository;
import com.ait.app.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	UserRepository userRepository;

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

}
