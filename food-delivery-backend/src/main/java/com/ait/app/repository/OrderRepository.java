
package com.ait.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

	List<Order> findByUserId(int userId);

	long countByDeliveryPartnerIdAndStatusIn(Integer deliveryPartnerId, List<String> statuses);

	Page<Order> findByUserIdOrderByCreatedAtDesc(int userId, Pageable pageable);
	List<Order> findByDeliveryPartnerId(Integer deliveryPartnerId);

	List<Order> findByDeliveryPartnerIdAndStatus(Integer deliveryPartnerId, String status);

	List<Order> findByDeliveryPartnerIdAndStatusNot(Integer deliveryPartnerId, String status);
}
