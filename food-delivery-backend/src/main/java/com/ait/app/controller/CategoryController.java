package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ait.app.Service.CategoryService;
import com.ait.app.model.Category;
import com.ait.app.requestBody.CategoryDto;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@PostMapping("/restaurant/{restaurantId}")
	public ResponseEntity<Category> addCategory(@PathVariable int restaurantId, @RequestBody CategoryDto categoryDto) {

		return categoryService.addCategory(restaurantId, categoryDto);
	}

	@PatchMapping("/{categoryId}")
	public ResponseEntity<Category> updateCategory(@PathVariable int categoryId, @RequestBody CategoryDto categoryDto) {

		return categoryService.updateCategory(categoryId, categoryDto);
	}

	@GetMapping("/{categoryId}")
	public ResponseEntity getCategoryById(@PathVariable int categoryId) {

		return categoryService.getCategoryById(categoryId);
	}

	@GetMapping("/restaurant/{restaurantId}")
	public ResponseEntity getAllCategories(@PathVariable int restaurantId) {

		return categoryService.getAllCategories(restaurantId);
	}

	@DeleteMapping("/{categoryId}")
	public ResponseEntity deleteCategory(@PathVariable int categoryId) {

		return categoryService.deleteCategory(categoryId);
	}
}