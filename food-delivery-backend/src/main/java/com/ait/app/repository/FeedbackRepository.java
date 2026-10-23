package com.ait.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

	boolean existsByUserIdAndOrderId(int userId, int orderId);
	Page<Feedback> findByRestaurantId(int restaurantId, Pageable pageable);
}