package com.ait.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	List<Category> findByRestaurantId(int restaurantId);
	
	Optional<Category> findByName(String category);

}