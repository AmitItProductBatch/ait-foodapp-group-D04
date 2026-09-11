package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.model.Restaurant;
import com.ait.app.requestBody.RestaurantDto;


public interface RestaurantService{
	
	ResponseEntity addRestaurant(RestaurantDto restaurantdto);

}
