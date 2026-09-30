package com.ait.app.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.RatingService;
import com.ait.app.customExceptionHandler.RestaurantException;
import com.ait.app.model.Order;
import com.ait.app.model.Rating;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.OrderRepository;
import com.ait.app.repository.RatingRepository;
import com.ait.app.repository.RestaurantRepo;
import com.ait.app.requestBody.RatingDto;

import jakarta.transaction.Transactional;

@Service
public class RatingServiceImpl implements RatingService {

	@Autowired
	RatingRepository ratingRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	RestaurantRepo restaurantRepo;

	@Override
	@Transactional
	public ResponseEntity<String> rateRestaurant(RatingDto dto) {

		// rating must be between 1 and 5
		if (dto.getRating() < 1 || dto.getRating() > 5) {
			throw new RestaurantException("Rating must be between 1 and 5", HttpStatus.BAD_REQUEST);
		}

		Optional<Restaurant> optionalRestaurant = restaurantRepo.findById(dto.getRestaurantId());

		if (optionalRestaurant.isEmpty()) {
			throw new RestaurantException("Restaurant not found", HttpStatus.NOT_FOUND);
		}

		Optional<Order> optionalOrder = orderRepository.findById(dto.getOrderId());

		if (optionalOrder.isEmpty()) {
			throw new RestaurantException("Order not found", HttpStatus.NOT_FOUND);
		}

		Restaurant restaurant = optionalRestaurant.get();
		Order order = optionalOrder.get();

		// the order must belong to the user who is giving the rating
		if (order.getUserId() != dto.getUserId()) {
			throw new RestaurantException("You can rate only your own orders", HttpStatus.FORBIDDEN);
		}

		// the order must be from this restaurant and must be delivered
		if (order.getRestaurantId() != dto.getRestaurantId() || !"DELIVERED".equalsIgnoreCase(order.getStatus())) {
			throw new RestaurantException("You can rate only a restaurant from which you have a delivered order",
					HttpStatus.BAD_REQUEST);
		}

		// if this order is already rated, update it. Otherwise create a new rating
		Optional<Rating> existingRating = ratingRepository.findByOrderId(dto.getOrderId());

		Rating rating;

		if (existingRating.isPresent()) {
			rating = existingRating.get();
		} else {
			rating = new Rating();
			rating.setUserId(dto.getUserId());
			rating.setRestaurantId(dto.getRestaurantId());
			rating.setOrderId(dto.getOrderId());
		}

		rating.setRating(dto.getRating());
		ratingRepository.save(rating);

		// recalculate the average rating of the restaurant
		List<Rating> ratings = ratingRepository.findByRestaurantId(dto.getRestaurantId());

		int total = 0;

		for (Rating r : ratings) {
			total = total + r.getRating();
		}

		restaurant.setRating((double) total / ratings.size());
		restaurantRepo.save(restaurant);

		if (existingRating.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body("Rating updated successfully");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body("Rating saved successfully");
	}

}