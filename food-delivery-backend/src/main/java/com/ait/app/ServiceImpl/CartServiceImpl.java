package com.ait.app.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.Service.CartService;
import com.ait.app.customExceptionHandler.CartException;
import com.ait.app.model.Cart;
import com.ait.app.model.Restaurant;
import com.ait.app.model.CartItems;
import com.ait.app.model.MenuItem;
import com.ait.app.model.User;
import com.ait.app.repository.CartItemRepository;
import com.ait.app.repository.CartRepository;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.RestaurantRepo;
import com.ait.app.repository.UserRepository;
import com.ait.app.requestBody.CartRequestDto;
import com.ait.app.requestBody.CartItemResponseDto;
import com.ait.app.requestBody.CartResponseDto;

@Service
public class CartServiceImpl implements CartService {

	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

	@Autowired
	CartRepository cartRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RestaurantRepo restaurantRepo;

	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	MenuItemRepository menuItemRepository;

	@Override
	public Cart createCart(int userId) {

		logger.info("Create cart request received. userId: {}", userId);

		Optional<User> optionalUser = userRepository.findById(userId);

		if (optionalUser.isEmpty()) {

			logger.error("Cart could not be created because user was not found. userId: {}", userId);

			throw new CartException("Cart creation failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		User user = optionalUser.get();

		Cart cart = new Cart();
		cart.setUser(user);

		try {

			Cart savedCart = cartRepository.save(cart);

			logger.info("Cart created successfully. userId: {}, cartId: {}", userId, savedCart.getCartid());

			return savedCart;

		} catch (Exception e) {

			logger.error("Cart could not be saved. userId: {}", userId);

			throw new CartException("Unable to create cart", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public CartResponseDto getCart(int userId) {

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
			itemResponse.setCartItemId(cartItem.getCartitemid());
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

	@Override
	public void addItemToCart(CartRequestDto dto) {
		System.out.println("0");
		int userId = dto.getUserId();
		int restaurantId = dto.getRestaurantId();
		int menuItemId = dto.getMenuItemId();
		int quantity = dto.getQuantity();
		System.out.println("1");
		Optional<User> userOptional = userRepository.findById(userId);

		if (userOptional.isEmpty()) {
			throw new CartException("User not found", HttpStatus.NOT_FOUND);
		}
		System.out.println("2");
		Optional<Restaurant> restaurantOptional = restaurantRepo.findById(restaurantId);

		if (restaurantOptional.isEmpty()) {
			throw new CartException("Restaurant not found", HttpStatus.NOT_FOUND);
		}
		System.out.println("3");
		Optional<MenuItem> menuItemOptional = menuItemRepository.findById(menuItemId);

		if (menuItemOptional.isEmpty()) {
			throw new CartException("Menu item not found", HttpStatus.NOT_FOUND);
		}
		System.out.println("4");
		User user = userOptional.get();
		Restaurant restaurant = restaurantOptional.get();
		MenuItem menuItem = menuItemOptional.get();

		Cart cart = cartRepository.findByUserId(userId).get();
		System.out.println("5");
		if (cart.getRestaurant().getId() != restaurantId) {
			throw new CartException("Your cart already contains items from another restaurant. Clear your cart first.",
					HttpStatus.CONFLICT);
		}
		System.out.println("6");
		double unitPrice = menuItem.getPrice();

		Optional<CartItems> existingItemOptional = cartItemRepository.findByCartAndMenuItem(cart, menuItem);

		CartItems cartItem;
		System.out.println("7");
		if (existingItemOptional.isPresent()) {
			cartItem=existingItemOptional.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			
		} else {
			cartItem = new CartItems();
			cartItem.setCart(cart);
			cartItem.setMenuItem(menuItem);
			cartItem.setQuantity(quantity);
			cartItem.setUnitprice(unitPrice);
		}
		System.out.println("9");
		cartItem.setSubtotal(cartItem.getUnitprice() * cartItem.getQuantity());

		cartItemRepository.save(cartItem);

	}

	@Override
	public void deleteFromCart(int cid) {
		Optional<Cart> optional = cartRepository.findById(cid);

		if (optional.isEmpty()) {
			throw new CartException("Cart not found for id " + cid, HttpStatus.NOT_FOUND);
		}

		Cart cart = optional.get();

		cartRepository.deleteById(cart.getCartid());

	}

}
