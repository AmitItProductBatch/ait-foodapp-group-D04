package com.ait.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
