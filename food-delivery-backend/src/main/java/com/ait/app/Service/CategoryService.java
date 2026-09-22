package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.model.Category;
import com.ait.app.requestBody.CategoryDto;

public interface CategoryService {

	ResponseEntity<Category> addCategory(int restaurantId, CategoryDto categoryDto);

	ResponseEntity<Category> updateCategory(int categoryId, CategoryDto categoryDto);

	ResponseEntity getCategoryById(int categoryId);

	ResponseEntity getAllCategories(int restaurantId);

	ResponseEntity deleteCategory(int categoryId);
}