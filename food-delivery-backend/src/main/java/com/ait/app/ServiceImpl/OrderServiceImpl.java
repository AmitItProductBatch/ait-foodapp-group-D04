package com.ait.app.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.app.Service.OrderService;
import com.ait.app.Service.OrderTotalService;
import com.ait.app.customExceptionHandler.OrderException;
import com.ait.app.event.OrderStatusChangedEvent;
import com.ait.app.model.Address;
import com.ait.app.model.Cart;
import com.ait.app.model.CartItems;
import com.ait.app.model.MenuItem;
import com.ait.app.model.Order;
import com.ait.app.model.OrderItem;
import com.ait.app.model.OrderStatus;
import com.ait.app.model.OrderStatusHistory;
import com.ait.app.model.Restaurant;
import com.ait.app.model.User;
import com.ait.app.repository.AddressRepository;
import com.ait.app.repository.CartItemRepository;
import com.ait.app.repository.CartRepository;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.OrderRepository;
import com.ait.app.repository.OrderStatusHistoryRepository;
import com.ait.app.repository.RestaurantRepo;
import com.ait.app.repository.UserRepository;
import com.ait.app.requestBody.OrderDto;
import com.ait.app.requestBody.OrderItemDto;
import com.ait.app.requestBody.OrderStatusUpdateDto;
import com.ait.app.requestBody.OrderTotalRequestDto;
import com.ait.app.response.OrderResponseDto;
import com.ait.app.response.OrderTotalResponseDto;
import com.ait.app.response.PriceCalculationResponse;

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

	@Autowired
	private OrderStatusHistoryRepository orderStatusHistoryRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;
  @Autowired
	private OrderTotalService orderTotalService;

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

		List<MenuItem> validMenuItems = new ArrayList<>();

		
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

		
			validMenuItems.add(menuItemObj);
		}

		
		OrderTotalRequestDto totalRequest = new OrderTotalRequestDto();

		totalRequest.setCartId(cart.getId());
		totalRequest.setRestaurantId(restaurantObj.getId());
		totalRequest.setAddressId(orderDto.getAddressId());

		OrderTotalResponseDto totalResponse = orderTotalService.calculateOrdertotal(totalRequest);

		double orderTotal = totalResponse.getOrderTotal();

		
		List<OrderItem> orderItems = new ArrayList<>();

		List<PriceCalculationResponse> calculatedItems = totalResponse.getItems();

		for (int i = 0; i < validMenuItems.size(); i++) {

			MenuItem menuItem = validMenuItems.get(i);

			CartItems cartItem = cartItems.get(i);

			PriceCalculationResponse priceItem = calculatedItems.get(i);

			OrderItem orderItem = new OrderItem();

			orderItem.setMenuItem(menuItem);

			orderItem.setItemName(menuItem.getName());

			orderItem.setUnitPrice(BigDecimal.valueOf(priceItem.getUnitPrice()));

			orderItem.setQuantity(cartItem.getQuantity());

			orderItem.setSubtotal(BigDecimal.valueOf(priceItem.getSubtotal()));

			orderItems.add(orderItem);
		}

		
		String deliveryAddressSnapshot = address.getAddressLabel() + ", " + address.getStreetAddress() + ", "
				+ address.getApartmentSuiteFloor() + ", " + address.getLandmark() + ", " + address.getCity() + ", "
				+ address.getPostalCode() + ", " + address.getDeliveryInstructions();

		
		Order order = new Order();

		order.setUserId(user.getId());
		order.setRestaurantId(restaurantObj.getId());
		order.setDeliveryAddressSnapshot(deliveryAddressSnapshot);

		order.setTotalAmount(orderTotal);

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
				user.getId(), orderTotal);

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

	@Override
	@Transactional
	public ResponseEntity updateOrderStatus(int orderId, OrderStatusUpdateDto statusUpdate) {
		if (statusUpdate == null || statusUpdate.getStatus() == null) {
			throw new OrderException("A target status is required", HttpStatus.BAD_REQUEST);
		}
		if (statusUpdate.getUserId() <= 0) {
			throw new OrderException("A valid acting user is required", HttpStatus.UNAUTHORIZED);
		}

		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if (optionalOrder.isEmpty()) {
			throw new OrderException("Order not found", HttpStatus.NOT_FOUND);
		}
		Order order = optionalOrder.get();

		Optional<User> optionalActor = userRepository.findById(statusUpdate.getUserId());
		if (optionalActor.isEmpty()) {
			throw new OrderException("Acting user not found", HttpStatus.UNAUTHORIZED);
		}
		User actor = optionalActor.get();

		if (!isPlatformAdmin(actor) && !ownsRestaurant(actor, order.getRestaurantId())) {
			throw new OrderException("Only the order restaurant or a platform administrator may update status",
					HttpStatus.FORBIDDEN);
		}

		OrderStatus current = parseStatus(order.getStatus());
		OrderStatus target = statusUpdate.getStatus();
		if (!current.canTransitionTo(target)) {
			throw new OrderException("Invalid order status transition from " + current + " to " + target,
					HttpStatus.BAD_REQUEST);
		}

		LocalDateTime changedAt = LocalDateTime.now();
		order.setStatus(target.name());
		orderRepository.save(order);
		orderStatusHistoryRepository.save(new OrderStatusHistory(orderId, current, target,
				actor.getId(), changedAt));
		eventPublisher.publishEvent(new OrderStatusChangedEvent(orderId, order.getUserId(), current, target));
		return ResponseEntity.ok(order);
	}

	private OrderStatus parseStatus(String status) {
		try {
			return OrderStatus.valueOf(status == null ? "" : status.trim().toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw new OrderException("Order has an unknown status", HttpStatus.CONFLICT);
		}
	}

	private boolean isPlatformAdmin(User actor) {
		String role = actor.getRoleName();
		return role != null && ("ADMIN".equalsIgnoreCase(role) || "PLATFORM_ADMIN".equalsIgnoreCase(role));
	}

	private boolean ownsRestaurant(User actor, int restaurantId) {
		if (actor.getRestaurant() != null) {
			for (Restaurant restaurant : actor.getRestaurant()) {
				if (restaurant != null && restaurant.getId() == restaurantId) {
					return true;
				}
			}
		}
		Optional<Restaurant> optionalRestaurant = restaurantRepo.findById(restaurantId);
		if (optionalRestaurant.isPresent()) {
			Restaurant restaurant = optionalRestaurant.get();
			return restaurant.getUser() != null && restaurant.getUser().getId() == actor.getId();
		}
		return false;
	}
}
