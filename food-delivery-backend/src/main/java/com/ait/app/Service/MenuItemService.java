package com.ait.app.Service;

import org.springframework.http.ResponseEntity;

import com.ait.app.dto.MenuItemDto;

public interface MenuItemService {

    ResponseEntity<?> addMenuItem(int restaurantId, MenuItemDto menuItemDto);

}