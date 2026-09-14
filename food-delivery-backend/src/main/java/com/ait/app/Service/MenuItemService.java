package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.dto.MenuItemDto;
import com.ait.app.model.MenuItem;

public interface MenuItemService {

	ResponseEntity<MenuItem> addMenuItem(int restaurantId, MenuItemDto menuItemDto);

}