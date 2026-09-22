package com.ait.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.CartItems;

public interface CartItemRepository extends JpaRepository<CartItems, Integer>{
    List<CartItems> findByCartId(int id);
}