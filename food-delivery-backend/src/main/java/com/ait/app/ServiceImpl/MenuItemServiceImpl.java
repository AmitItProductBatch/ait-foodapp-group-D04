package com.ait.app.ServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.MenuItemService;
import com.ait.app.customExceptionHandler.RestaurantException;
import com.ait.app.model.MenuItem;
import com.ait.app.model.Restaurant;
import com.ait.app.model.User;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.RestaurantRepo;
import com.ait.app.requestBody.MenuItemDto;

@Service
public class MenuItemServiceImpl implements MenuItemService {

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private RestaurantRepo restaurantRepo;

	@Override
	public ResponseEntity<MenuItem> addMenuItem(int restaurantId, MenuItemDto menuItemDto) {

		if (menuItemDto.getName() == null || menuItemDto.getName().isBlank()) {
			throw new RestaurantException("Name is required", HttpStatus.BAD_REQUEST);
		}

		if (menuItemDto.getDescription() == null || menuItemDto.getDescription().isBlank()) {
			throw new RestaurantException("Description is required", HttpStatus.BAD_REQUEST);
		}

		if (menuItemDto.getPrice() <= 0) {
			throw new RestaurantException("Price must be greater than 0", HttpStatus.BAD_REQUEST);
		}

		if (menuItemDto.getCategory() == null || menuItemDto.getCategory().isBlank()) {
			throw new RestaurantException("Category is required", HttpStatus.BAD_REQUEST);
		}

		Optional<Restaurant> optionalRestaurant = restaurantRepo.findById(restaurantId);

		if (optionalRestaurant.isEmpty()) {
			throw new RestaurantException("Restaurant not found", HttpStatus.NOT_FOUND);
		}

		Restaurant restaurant = optionalRestaurant.get();

		User user = restaurant.getUser();

		if (user == null) {
			throw new RestaurantException("No user is linked with this restaurant", HttpStatus.UNAUTHORIZED);
		}

		if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
			throw new RestaurantException("Only restaurant admin can add menu items", HttpStatus.FORBIDDEN);
		}

		if (menuItemRepository.existsByRestaurantIdAndName(restaurantId, menuItemDto.getName())) {

			throw new RestaurantException("Menu item with this name already exists for this restaurant",
					HttpStatus.CONFLICT);
		}

		MenuItem menuItem = new MenuItem();

		menuItem.setName(menuItemDto.getName());
		menuItem.setDescription(menuItemDto.getDescription());
		menuItem.setPrice(menuItemDto.getPrice());
		menuItem.setAvailability(menuItemDto.isAvailability());
		menuItem.setCategory(menuItemDto.getCategory());
		menuItem.setRestaurant(restaurant);

		MenuItem savedItem = menuItemRepository.save(menuItem);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
	}

	@Override
	public ResponseEntity updateMenuItem(int itemId, MenuItemDto menuItemDto) {

		Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(itemId);

		if (optionalMenuItem.isEmpty()) {

			throw new RestaurantException("Menu item not found", HttpStatus.NOT_FOUND);
		}

		MenuItem menuItem = optionalMenuItem.get();

		if (menuItemDto.getPrice() > 0) {
			menuItem.setPrice(menuItemDto.getPrice());
		}

		if (menuItemDto.getDescription() != null) {

			menuItem.setDescription(menuItemDto.getDescription());
		}

		if (menuItemDto.isAvailability() == true) {
		    menuItem.setAvailability(true);
		} 
		else {
		    menuItem.setAvailability(false);
		}
		
		MenuItem updatedItem = menuItemRepository.save(menuItem);

		return ResponseEntity.status(HttpStatus.OK).body(updatedItem);
	}
}