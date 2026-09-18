package com.ait.app.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.app.Service.CartItemService;
import com.ait.app.customExceptionHandler.CartItemServiceException;
import com.ait.app.model.Cart;
import com.ait.app.model.CartItems;
import com.ait.app.model.MenuItem;
import com.ait.app.repository.CartItemRepository;
import com.ait.app.repository.CartRepository;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.requestBody.CartItemDto;

@Service
public class CartItemServiceImpl implements CartItemService{
	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	MenuItemRepository menuItemRepo;

	@Override
	public CartItems createCartItem(CartItemDto dto) {
		Optional<Cart> cartOptional = cartRepository.findById(dto.getCartId());

		if (cartOptional.isEmpty()) {
			throw new CartItemServiceException("Cart not found", HttpStatus.NOT_FOUND);
		}

		Optional<MenuItem> foodItemOptional = menuItemRepo.findById(dto.getMenuItemId());

		if (foodItemOptional.isEmpty()) {
			throw new CartItemServiceException("Menu item not found", HttpStatus.NOT_FOUND);
		}

		if (dto.getQuantity() < 1) {
			throw new CartItemServiceException("Quantity must be at least 1", HttpStatus.BAD_REQUEST);
		}

		Cart cart = cartOptional.get();

		MenuItem menuItems = foodItemOptional.get();

		List<CartItems> existingItems = cartItemRepository.findByCartId(dto.getCartId());

		for (CartItems items : existingItems) {

			if (items.getMenuItem().getId() == dto.getMenuItemId()) {

				throw new CartItemServiceException("Menu item already exists in cart", HttpStatus.CONFLICT);
			}
		}
		CartItems cartItem = new CartItems();

		cartItem.setCart(cart);
		cartItem.setMenuItem(menuItems);
		cartItem.setQuantity(dto.getQuantity());

		double unitPrice = menuItems.getPrice();
		double subtotal = unitPrice * dto.getQuantity();

		cartItem.setUnitprice(unitPrice);
		cartItem.setSubtotal(subtotal);

	return	cartItemRepository.save(cartItem);

	}
	@Override
	public void deleteCartItem(int id) {
		Optional<CartItems> optional = cartItemRepository.findById(id);

		if (optional.isEmpty()) {
			throw new CartItemServiceException("Cart item not found in your active cart with id:" +id, HttpStatus.NOT_FOUND);
		}

		Cart cart = optional.get().getCart();

		cartItemRepository.deleteById(id);

	}
}
