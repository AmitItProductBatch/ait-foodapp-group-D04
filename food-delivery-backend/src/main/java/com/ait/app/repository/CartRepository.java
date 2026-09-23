package com.ait.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
  
	  Optional<Cart> findByUserId(int userId);
  
	  boolean existsByUserId(int userId);

  
}
