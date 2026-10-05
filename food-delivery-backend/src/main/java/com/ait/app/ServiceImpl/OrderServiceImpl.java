package com.ait.app.ServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.app.Service.OrderService;
import com.ait.app.customExceptionHandler.OrderException;
import com.ait.app.model.Address;
import com.ait.app.model.Cart;
import com.ait.app.model.CartItems;
import com.ait.app.model.MenuItem;
import com.ait.app.model.Order;
import com.ait.app.model.OrderItem;
import com.ait.app.model.Restaurant;
import com.ait.app.model.User;
import com.ait.app.repository.AddressRepository;
import com.ait.app.repository.CartItemRepository;
import com.ait.app.repository.CartRepository;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.OrderRepository;
import com.ait.app.repository.RestaurantRepo;
import com.ait.app.repository.UserRepository;
import com.ait.app.requestBody.OrderDto;
import com.ait.app.requestBody.OrderItemDto;
import com.ait.app.response.OrderResponseDto;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private RestaurantRepo restaurantRepo;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Override
	@Transactional
	public ResponseEntity placeOrder(OrderDto orderDto) {

		logger.info("Place order request received for userId: {}", orderDto.getUserId());

		Optional<User> optionalUser = userRepository.findById(orderDto.getUserId());

		if (optionalUser.isEmpty()) {
			logger.warn("User not found for order placement. userId: {}", orderDto.getUserId());
			throw new OrderException("User not found", HttpStatus.NOT_FOUND);
		}

		User user = optionalUser.get();

		Optional<Cart> optionalCart = cartRepository.findByUserId(user.getId());

		if (optionalCart.isEmpty()) {
			logger.warn("Cart not found for order placement. userId: {}", user.getId());
			throw new OrderException("Cart not found", HttpStatus.NOT_FOUND);
		}

		Cart cart = optionalCart.get();

		List<CartItems> cartItems = cartItemRepository.findByCartId(cart.getId());

		if (cartItems == null || cartItems.isEmpty()) {
			logger.warn("Cart is empty. Order cannot be placed. userId: {}", user.getId());
			throw new OrderException("Cart is empty", HttpStatus.BAD_REQUEST);
		}

		Restaurant restaurant = cart.getRestaurant();

		if (restaurant == null) {
			logger.error("Restaurant information is missing in cart. userId: {}", user.getId());
			throw new OrderException("Restaurant not found in cart", HttpStatus.BAD_REQUEST);
		}

		Optional<Restaurant> optionalRestaurant = restaurantRepo.findById(restaurant.getId());

		if (optionalRestaurant.isEmpty()) {
			logger.error("Restaurant not found while placing order. restaurantId: {}", restaurant.getId());
			throw new OrderException("Restaurant not found", HttpStatus.NOT_FOUND);
		}

		Restaurant restaurantObj = optionalRestaurant.get();

		if (!restaurantObj.isActive()) {
			logger.warn("Restaurant is closed. Order cannot be placed. restaurantId: {}", restaurantObj.getId());
			throw new OrderException("Restaurant is closed", HttpStatus.BAD_REQUEST);
		}

		if (!restaurantObj.isApproved()) {
			logger.warn("Restaurant is not approved. restaurantId: {}", restaurantObj.getId());
			throw new OrderException("Restaurant is not approved", HttpStatus.BAD_REQUEST);
		}

		Optional<Address> optionalAddress = addressRepository.findById(orderDto.getAddressId());

		if (optionalAddress.isEmpty()) {
			logger.warn("Delivery address not found. addressId: {}", orderDto.getAddressId());
			throw new OrderException("Address not found", HttpStatus.NOT_FOUND);
		}

		Address address = optionalAddress.get();

		if (address.getUser() == null || address.getUser().getId() != user.getId()) {

			logger.warn("Selected address does not belong to user. userId: {}, addressId: {}", user.getId(),
					orderDto.getAddressId());

			throw new OrderException("Address does not belong to this user", HttpStatus.BAD_REQUEST);
		}

		double totalAmount = 0;

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItems cartItem : cartItems) {

			if (cartItem.getQuantity() <= 0) {

				logger.warn("Invalid item quantity in cart. userId: {}", user.getId());

				throw new OrderException("Quantity must be greater than zero", HttpStatus.BAD_REQUEST);
			}

			MenuItem menuItem = cartItem.getMenuItem();

			if (menuItem == null) {

				logger.error("Menu item information is missing in cart. userId: {}", user.getId());

				throw new OrderException("Menu item not found in cart", HttpStatus.NOT_FOUND);
			}

			Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(menuItem.getId());

			if (optionalMenuItem.isEmpty()) {

				logger.error("Menu item not found while placing order. menuItemId: {}", menuItem.getId());

				throw new OrderException("Menu item not found: " + menuItem.getId(), HttpStatus.NOT_FOUND);
			}

			MenuItem menuItemObj = optionalMenuItem.get();

			if (menuItemObj.getRestaurant() == null || menuItemObj.getRestaurant().getId() != restaurantObj.getId()) {

				logger.warn("Menu item does not belong to selected restaurant. menuItemId: {}, restaurantId: {}",
						menuItemObj.getId(), restaurantObj.getId());

				throw new OrderException("Menu item does not belong to selected restaurant", HttpStatus.BAD_REQUEST);
			}

			if (!menuItemObj.isAvailability()) {

				logger.warn("Menu item is unavailable. menuItemId: {}", menuItemObj.getId());

				throw new OrderException("Menu item is unavailable: " + menuItemObj.getName(), HttpStatus.BAD_REQUEST);
			}

			if (!menuItemObj.isActive()) {

				logger.warn("Menu item is inactive. menuItemId: {}", menuItemObj.getId());

				throw new OrderException("Menu item is inactive: " + menuItemObj.getName(), HttpStatus.BAD_REQUEST);
			}

			double unitPrice = menuItemObj.getPrice();

			double subtotal = unitPrice * cartItem.getQuantity();

			totalAmount = totalAmount + subtotal;

			OrderItem orderItem = new OrderItem();

			orderItem.setMenuItem(menuItemObj);
			orderItem.setItemName(menuItemObj.getName());
			orderItem.setUnitPrice(BigDecimal.valueOf(unitPrice));
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(BigDecimal.valueOf(subtotal));

			orderItems.add(orderItem);
		}

		String deliveryAddressSnapshot = address.getAddressLabel() + ", " + address.getStreetAddress() + ", "
				+ address.getApartmentSuiteFloor() + ", " + address.getLandmark() + ", " + address.getCity() + ", "
				+ address.getPostalCode() + ", " + address.getDeliveryInstructions();

		Order order = new Order();

		order.setUserId(user.getId());
		order.setRestaurantId(restaurantObj.getId());
		order.setDeliveryAddressSnapshot(deliveryAddressSnapshot);
		order.setTotalAmount(totalAmount);
		order.setStatus("PLACED");
		order.setPaymentStatus("PENDING");

		for (OrderItem orderItem : orderItems) {
			orderItem.setOrder(order);
		}

		order.setOrderItems(orderItems);

		Order savedOrder;

		try {

			savedOrder = orderRepository.save(order);

		} catch (Exception e) {

			logger.error("Order could not be saved. userId: {}, restaurantId: {}", user.getId(), restaurantObj.getId());

			throw new OrderException("Unable to save order", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {

			cartItemRepository.deleteAll(cartItems);

		} catch (Exception e) {

			logger.error("Order created but cart items could not be cleared. orderId: {}", savedOrder.getId());

			throw new OrderException("Order placed but cart could not be cleared", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		logger.info("Order placed successfully. orderId: {}, userId: {}, totalAmount: {}", savedOrder.getId(),
				user.getId(), totalAmount);

		return ResponseEntity.status(HttpStatus.CREATED).body("Your order placed successfully");
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity viewOrder(int orderId) {

		logger.info("View order request received. orderId: {}", orderId);

		Optional<Order> optionalOrder = orderRepository.findById(orderId);

		if (optionalOrder.isEmpty()) {

			logger.warn("Order not found. orderId: {}", orderId);

			throw new OrderException("Order not found", HttpStatus.NOT_FOUND);
		}

		Order order = optionalOrder.get();

		OrderResponseDto response = new OrderResponseDto();

		response.setOrderId(order.getId());
		response.setUserId(order.getUserId());
		response.setRestaurantId(order.getRestaurantId());
		response.setDeliveryAddressSnapshot(order.getDeliveryAddressSnapshot());
		response.setTotalAmount(order.getTotalAmount());
		response.setStatus(order.getStatus());
		response.setPaymentStatus(order.getPaymentStatus());
		response.setCreatedAt(order.getCreatedAt());

		List<OrderItemDto> itemResponses = new ArrayList<>();

		if (order.getOrderItems() != null) {

			for (OrderItem orderItem : order.getOrderItems()) {

				OrderItemDto itemDto = new OrderItemDto();

				itemDto.setMenuItemId(orderItem.getMenuItem().getId());
				itemDto.setItemName(orderItem.getItemName());
				itemDto.setUnitPrice(orderItem.getUnitPrice());
				itemDto.setQuantity(orderItem.getQuantity());
				itemDto.setSubtotal(orderItem.getSubtotal());

				itemResponses.add(itemDto);
			}
		}

		response.setItems(itemResponses);

		logger.info("Order details retrieved successfully. orderId: {}", orderId);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}