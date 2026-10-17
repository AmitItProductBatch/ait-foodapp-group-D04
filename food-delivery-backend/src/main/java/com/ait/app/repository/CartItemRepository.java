package com.ait.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Cart;
import com.ait.app.model.CartItems;
import com.ait.app.model.MenuItem;

public interface CartItemRepository extends JpaRepository<CartItems, Integer>{
    List<CartItems> findByCartId(int id);

	Optional<CartItems> findByCartAndMenuItem(Cart cart, MenuItem menuItem);
}