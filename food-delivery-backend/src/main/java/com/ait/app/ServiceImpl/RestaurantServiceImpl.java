package com.ait.app.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.RestaurantService;
import com.ait.app.customExceptionHandler.RestaurantException;
import com.ait.app.model.Cuisine;
import com.ait.app.model.Restaurant;
import com.ait.app.model.User;
import com.ait.app.repository.RestaurantRepo;
import com.ait.app.repository.UserRepository;
import com.ait.app.requestBody.RestaurantDto;

import com.ait.app.response.RestaurantDetailsResponse;


@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	RestaurantRepo restaurantrepo;
	
	@Autowired
	UserRepository userrepo;

	@Override
	public ResponseEntity addRestaurant(RestaurantDto restaurantdto) {
		
		if(restaurantdto.getName()==null||restaurantdto.getName().trim().isEmpty()||
		   restaurantdto.getAddress()==null||restaurantdto.getAddress().trim().isEmpty()||
		   restaurantdto.getContactNo()==null||restaurantdto.getContactNo().trim().isEmpty()) {
			
			throw new RestaurantException("All fields are required", HttpStatus.BAD_REQUEST);
		}
		try {
			User user = userrepo.findById(restaurantdto.getUserId()).get();
			
			 Restaurant restaurant = new Restaurant();
		      restaurant.setName(restaurantdto.getName());
		      restaurant.setAddress(restaurantdto.getAddress());
		      restaurant.setContactNo(restaurantdto.getContactNo());
			
			restaurant.setUser(user);
			
			 List<Cuisine> cuisineList = restaurantdto.getCuisine();

		        if (cuisineList != null) {

		            for (Cuisine cuisine : cuisineList) {

		              
		                cuisine.setRestaurant(restaurant);
		            }

		            restaurant.setCuisine(cuisineList);
		        }

		        Restaurant res = restaurantrepo.save(restaurant);
		        
		        Map data = new HashMap<>();
		        data.put("message", "Restaurant Saved Succesfully");
		        data.put("restaurant", res);

		        return ResponseEntity.status(HttpStatus.ACCEPTED).body(data);

		    } catch (Exception e) {

		        throw new RestaurantException("Failed to save Restaurant",HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}
	
	
	@Override
	public ResponseEntity getRestaurantById(int id) {

		try {

			Restaurant restaurant = restaurantrepo.findById(id)
					.orElseThrow(() ->
							new RestaurantException(
									"Restaurant not found with ID: " + id,
									HttpStatus.NOT_FOUND
							));

			RestaurantDetailsResponse responseData =
					new RestaurantDetailsResponse();

			responseData.setName(restaurant.getName());
			responseData.setAddress(restaurant.getAddress());
			responseData.setContactNo(restaurant.getContactNo());

			List<String> cuisineNames = new ArrayList<>();

			if (restaurant.getCuisine() != null) {

				for (Cuisine cuisine : restaurant.getCuisine()) {

					if (cuisine != null && cuisine.getName() != null) {
						cuisineNames.add(cuisine.getName());
					}
				}
			}

			responseData.setCuisine(cuisineNames);
			
			responseData.setRating(4.0);
			responseData.setOperatingHours("10:00 AM - 11:00 PM");
			
			
			
			

			Map data = new HashMap<>();
			data.put("message", "Restaurant Details Fetched Successfully");
			data.put("restaurant", responseData);

			return ResponseEntity.status(HttpStatus.OK).body(data);

		} catch (RestaurantException e) {

			throw e;

		} catch (Exception e) {

			throw new RestaurantException(
					"Failed to fetch restaurant details: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR
			);
		}
	}

}
