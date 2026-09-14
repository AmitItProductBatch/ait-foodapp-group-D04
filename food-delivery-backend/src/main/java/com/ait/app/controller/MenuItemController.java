package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ait.app.Service.MenuItemService;
import com.ait.app.dto.MenuItemDto;
import com.ait.app.model.MenuItem;

@RestController
@RequestMapping("/api/restaurants")
public class MenuItemController {

	@Autowired
	private MenuItemService menuItemService;

	@PostMapping("/{restaurantId}/menu")
	public ResponseEntity<MenuItem> addMenuItem(@PathVariable int restaurantId, @RequestBody MenuItemDto menuItemDto) {

		return menuItemService.addMenuItem(restaurantId, menuItemDto);
	}
}