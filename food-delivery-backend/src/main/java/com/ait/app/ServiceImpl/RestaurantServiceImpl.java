package com.ait.app.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	RestaurantRepo restaurantrepo;

	@Autowired
	UserRepository userrepo;;

	@Override
	public ResponseEntity addRestaurant(RestaurantDto restaurantdto) {

		if (restaurantdto.getName() == null || restaurantdto.getName().trim().isEmpty()
				|| restaurantdto.getAddress() == null || restaurantdto.getAddress().trim().isEmpty()
				|| restaurantdto.getContactNo() == null || restaurantdto.getContactNo().trim().isEmpty()) {

			throw new RestaurantException("All fields are required", HttpStatus.BAD_REQUEST);
		}
		try {
			User user = userrepo.findById(restaurantdto.getUserId()).get();

			Restaurant restaurant = new Restaurant();
			restaurant.setName(restaurantdto.getName());
			restaurant.setAddress(restaurantdto.getAddress());
			restaurant.setContactNo(restaurantdto.getContactNo());
			restaurant.setRating(restaurantdto.getRating());
			restaurant.setActive(restaurantdto.isActive());
			restaurant.setApproved(restaurantdto.isApproved());

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

			throw new RestaurantException("Failed to save Restaurant", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Page<RestaurantDto> getRestaurants(Double rating, String location, String cuisine, String menuItem,
			Pageable pageable) {

		try {

			if (location == null) {
				location = "";
			}

			if (cuisine == null) {
				cuisine = "";
			}

			if (rating == null) {
				rating = 0.0;
			}

			if (menuItem == null) {
				menuItem = "";
			}
			Page<Restaurant> restaurants = restaurantrepo.findRestaurants(rating, location, cuisine, menuItem,
					pageable);

			if (restaurants.isEmpty()) {

				throw new RestaurantException("No Restaurant Found", HttpStatus.NOT_FOUND);
			}

			List<RestaurantDto> list = new ArrayList<>();

			for (Restaurant r : restaurants) {

				RestaurantDto dto = new RestaurantDto();

				if (r.getUser() != null) {
					dto.setUserId(r.getUser().getId());
				}
				dto.setName(r.getName());
				dto.setAddress(r.getAddress());
				dto.setContactNo(r.getContactNo());
				dto.setCuisine(r.getCuisine());
				dto.setMenuItems(r.getMenuItems());
				dto.setRating(r.getRating());
				dto.setActive(r.isActive());
				dto.setApproved(r.isApproved());

				list.add(dto);
			}

			return new PageImpl<>(list, pageable, restaurants.getTotalElements());

		} catch (Exception e) {
			throw new RestaurantException("Unable to fetch restaurant", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
