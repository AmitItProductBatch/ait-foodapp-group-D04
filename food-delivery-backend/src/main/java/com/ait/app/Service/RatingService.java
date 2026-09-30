package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.requestBody.RatingDto;

public interface RatingService {

	ResponseEntity<String> rateRestaurant(RatingDto dto);

}