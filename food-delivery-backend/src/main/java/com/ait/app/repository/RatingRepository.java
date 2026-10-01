package com.ait.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

	Optional<Rating> findByOrderId(int orderId);

	List<Rating> findByRestaurantId(int restaurantId);

}