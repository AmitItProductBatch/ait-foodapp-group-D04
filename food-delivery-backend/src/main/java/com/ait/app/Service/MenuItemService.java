package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.model.MenuItem;
import com.ait.app.requestBody.MenuItemDto;

public interface MenuItemService {

    ResponseEntity<MenuItem> addMenuItem(int restaurantId, MenuItemDto menuItemDto);
    
     ResponseEntity deleteMenuItem(int itemId, int userId);
}