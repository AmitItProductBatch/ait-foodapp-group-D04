package com.ait.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ait.app.model.Restaurant;

public interface RestaurantRepo extends JpaRepository<Restaurant, Integer> {

	@Query("""
			    SELECT DISTINCT r
			    FROM Restaurant r
			    LEFT JOIN r.cuisine c
			    LEFT JOIN r.menuItems m
			    WHERE r.active = true
			    AND r.approved = true
			    AND (:rating = 0 OR r.rating >= :rating)
			    AND (:location = '' OR r.address LIKE CONCAT('%', :location, '%'))
			    AND (:cuisine = '' OR c.name LIKE CONCAT('%', :cuisine, '%'))
			    AND (:menuItem = '' OR m.name LIKE CONCAT('%', :menuItem, '%'))
			""")
	Page<Restaurant> findRestaurants(Double rating, String location, String cuisine, String menuItem,
			Pageable pageable);

}
