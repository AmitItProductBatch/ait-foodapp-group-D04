package com.ait.app.Service;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.RestaurantDto;



public interface RestaurantService{
	
	ResponseEntity addRestaurant(RestaurantDto restaurantdto);
	
    Page<RestaurantDto> getRestaurants(
	            Double rating,
	            String location,
	            String cuisine,
	            String menuItem,
	            Pageable pageable);
	
	
	

}
