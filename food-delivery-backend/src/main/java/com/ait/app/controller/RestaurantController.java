package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.RestaurantService;
import com.ait.app.requestBody.RestaurantDto;

@RestController
public class RestaurantController {
	
	@Autowired
	RestaurantService restaurantService;
	
	@PostMapping("saveRestaurant")
	ResponseEntity saveRestaurant(@RequestBody RestaurantDto dto) {
		
		return restaurantService.addRestaurant(dto);
	}

}
