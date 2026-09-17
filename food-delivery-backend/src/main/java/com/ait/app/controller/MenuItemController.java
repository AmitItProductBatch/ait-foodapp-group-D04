package com.ait.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ait.app.Service.MenuItemService;
import com.ait.app.model.MenuItem;
import com.ait.app.requestBody.MenuItemDto;
import com.ait.app.response.MenuResponse;

@RestController
@RequestMapping("/api/restaurants")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping("/{restaurantId}/menu")
    public ResponseEntity<MenuItem> addMenuItem(
            @PathVariable int restaurantId,
            @RequestBody MenuItemDto menuItemDto) {

        return menuItemService.addMenuItem(restaurantId, menuItemDto);
    }

    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<List<MenuResponse>> getRestaurantMenu(
            @PathVariable int restaurantId) {

        return ResponseEntity.ok(
                menuItemService.getRestaurantMenu(restaurantId));
    }
}