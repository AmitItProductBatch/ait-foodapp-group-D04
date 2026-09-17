package com.ait.app.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ait.app.model.MenuItem;
import com.ait.app.requestBody.MenuItemDto;
import com.ait.app.response.MenuResponse;

public interface MenuItemService {

    ResponseEntity<MenuItem> addMenuItem(int restaurantId, MenuItemDto menuItemDto);
    List<MenuResponse> getRestaurantMenu(int restaurantId);
}