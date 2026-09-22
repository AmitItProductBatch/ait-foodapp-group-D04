package com.ait.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

    boolean existsByRestaurantIdAndName(int restaurantId, String name);

    List<MenuItem> findByRestaurantIdAndAvailabilityTrue(int restaurantId);

    List<MenuItem> findByRestaurantIdAndActiveTrue(int restaurantId);
    boolean existsByCategoryId(int categoryId);
}