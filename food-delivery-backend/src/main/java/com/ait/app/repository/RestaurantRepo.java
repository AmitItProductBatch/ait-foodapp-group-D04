package com.ait.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Restaurant;

public interface RestaurantRepo extends JpaRepository<Restaurant, Integer>{

}
