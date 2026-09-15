package com.ait.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ait.app.model.Cart;
import com.ait.app.model.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	boolean existsByUserId(int userId);
	 
}
