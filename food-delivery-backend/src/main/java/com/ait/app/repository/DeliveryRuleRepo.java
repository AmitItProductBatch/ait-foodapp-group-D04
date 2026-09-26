package com.ait.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.DeliveryRules;

public interface DeliveryRuleRepo extends JpaRepository<DeliveryRules, Integer>{
	
	List<DeliveryRules> findByActive(boolean active);

}
