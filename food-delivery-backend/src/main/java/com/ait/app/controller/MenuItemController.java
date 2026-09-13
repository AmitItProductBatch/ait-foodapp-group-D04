package com.ait.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ait.app.Service.MenuItemService;
import com.ait.app.dto.MenuItemDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurants")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping("/{restaurantId}/menu")
    public ResponseEntity<?> addMenuItem(
            @PathVariable int restaurantId,
            @Valid @RequestBody MenuItemDto menuItemDto) {

        return menuItemService.addMenuItem(restaurantId, menuItemDto);
    }
}