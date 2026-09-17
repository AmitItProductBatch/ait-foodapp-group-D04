package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.model.MenuItem;
import com.ait.app.requestBody.MenuItemDto;

public interface MenuItemService {



	ResponseEntity updateMenuItem(int itemId, MenuItemDto menuItemDto);
    ResponseEntity<MenuItem> addMenuItem(int restaurantId, MenuItemDto menuItemDto);

	ResponseEntity getItemDetails(int itemId);
    
     ResponseEntity deleteMenuItem(int itemId, int userId);
}