package com.ait.app.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.CategoryService;
import com.ait.app.customExceptionHandler.RestaurantException;
import com.ait.app.model.Category;
import com.ait.app.model.Restaurant;
import com.ait.app.repository.CategoryRepository;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.RestaurantRepo;
import com.ait.app.requestBody.CategoryDto;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	RestaurantRepo restaurantRepo;

	@Autowired
	MenuItemRepository menuItemRepository;

	@Override
	public ResponseEntity<Category> addCategory(int restaurantId, CategoryDto categoryDto) {

		if (restaurantId <= 0) {
			throw new RestaurantException("Invalid restaurant id", HttpStatus.BAD_REQUEST);
		}

		if (categoryDto == null) {
			throw new RestaurantException("Category details are required", HttpStatus.BAD_REQUEST);
		}

		if (categoryDto.getName() == null || categoryDto.getName().isEmpty()) {
			throw new RestaurantException("Category name is required", HttpStatus.BAD_REQUEST);
		}

		Optional<Restaurant> optionalRestaurant = restaurantRepo.findById(restaurantId);

		if (optionalRestaurant.isEmpty()) {
			throw new RestaurantException("Restaurant not found", HttpStatus.NOT_FOUND);
		}

		Restaurant restaurant = optionalRestaurant.get();

		if (restaurant.getUser() == null) {
			throw new RestaurantException("No user is linked with this restaurant", HttpStatus.UNAUTHORIZED);
		}

		if (!restaurant.getUser().getRole().equals("ADMIN")) {
			throw new RestaurantException("Only admin can add category", HttpStatus.FORBIDDEN);
		}

		List<Category> categories = categoryRepository.findByRestaurantId(restaurantId);

		for (Category c : categories) {
			if (c.getName().equalsIgnoreCase(categoryDto.getName())) {
				throw new RestaurantException("Category already exists for this restaurant", HttpStatus.BAD_REQUEST);
			}
		}

		Category category = new Category();
		category.setName(categoryDto.getName());
		category.setRestaurant(restaurant);

		Category savedCategory = categoryRepository.save(category);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
	}

	@Override
	public ResponseEntity<Category> updateCategory(int categoryId, CategoryDto categoryDto) {

		if (categoryId <= 0) {
			throw new RestaurantException("Invalid category id", HttpStatus.BAD_REQUEST);
		}

		if (categoryDto == null) {
			throw new RestaurantException("Category details are required", HttpStatus.BAD_REQUEST);
		}

		if (categoryDto.getName() == null || categoryDto.getName().isEmpty()) {
			throw new RestaurantException("Category name is required", HttpStatus.BAD_REQUEST);
		}

		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

		if (optionalCategory.isEmpty()) {
			throw new RestaurantException("Category not found", HttpStatus.NOT_FOUND);
		}

		Category category = optionalCategory.get();
		category.setName(categoryDto.getName());

		Category updatedCategory = categoryRepository.save(category);

		return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
	}

	@Override
	public ResponseEntity getCategoryById(int categoryId) {

		if (categoryId <= 0) {
			throw new RestaurantException("Invalid category id", HttpStatus.BAD_REQUEST);
		}

		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

		if (optionalCategory.isEmpty()) {
			throw new RestaurantException("Category not found", HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.status(HttpStatus.OK).body(optionalCategory.get());
	}

	@Override
	public ResponseEntity getAllCategories(int restaurantId) {

		if (restaurantId <= 0) {
			throw new RestaurantException("Invalid restaurant id", HttpStatus.BAD_REQUEST);
		}

		Optional<Restaurant> optionalRestaurant = restaurantRepo.findById(restaurantId);

		if (optionalRestaurant.isEmpty()) {
			throw new RestaurantException("Restaurant not found", HttpStatus.NOT_FOUND);
		}

		List<Category> categories = categoryRepository.findByRestaurantId(restaurantId);

		if (categories.isEmpty()) {
			throw new RestaurantException("No categories are linked with this restaurant", HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.status(HttpStatus.OK).body(categories);
	}

	@Override
	public ResponseEntity deleteCategory(int categoryId) {

		if (categoryId <= 0) {
			throw new RestaurantException("Invalid category id", HttpStatus.BAD_REQUEST);
		}

		Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

		if (optionalCategory.isEmpty()) {
			throw new RestaurantException("Category not found", HttpStatus.NOT_FOUND);
		}

		boolean menuItemExists = menuItemRepository.existsByCategoryId(categoryId);

		if (menuItemExists) {
			throw new RestaurantException("Category cannot be deleted because menu items are linked with it",HttpStatus.BAD_REQUEST);
		}

		Category category = optionalCategory.get();

		categoryRepository.delete(category);

		return ResponseEntity.status(HttpStatus.OK).body("Category deleted successfully");
	}
}