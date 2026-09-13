package com.ait.app.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.MenuItemService;
import com.ait.app.customExceptionHandler.RestaurantException;
import com.ait.app.dto.MenuItemDto;
import com.ait.app.model.MenuItem;
import com.ait.app.model.Restaurant;
import com.ait.app.model.User;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.RestaurantRepo;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepo restaurantRepo;

    @Override
    public ResponseEntity addMenuItem(int restaurantId, MenuItemDto menuItemDto) {

        // 1. Check whether restaurant exists
        Restaurant restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() ->
                        new RestaurantException("Restaurant not found", HttpStatus.NOT_FOUND));

        // 2. Check whether restaurant has a linked user
        User user = restaurant.getUser();

        if (user == null) {
            throw new RestaurantException(
                    "No user is linked with this restaurant",
                    HttpStatus.UNAUTHORIZED);
        }

        // 3. Check whether linked user is ADMIN
        if (user.getRole() == null ||
                !user.getRole().equalsIgnoreCase("ADMIN")) {

            throw new RestaurantException(
                    "Only restaurant admin can add menu items",
                    HttpStatus.FORBIDDEN);
        }

        // 4. Check duplicate menu item name for same restaurant
        if (menuItemRepository.existsByRestaurantIdAndName(
                restaurantId, menuItemDto.getName())) {

            throw new RestaurantException(
                    "Menu item with this name already exists for this restaurant",
                    HttpStatus.CONFLICT);
        }

        // 5. Create MenuItem
        MenuItem menuItem = new MenuItem();

        menuItem.setName(menuItemDto.getName());
        menuItem.setDescription(menuItemDto.getDescription());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setAvailability(menuItemDto.isAvailability());
        menuItem.setCategory(menuItemDto.getCategory());

        // 6. Connect MenuItem with Restaurant
        menuItem.setRestaurant(restaurant);

        // 7. Save MenuItem
        MenuItem savedItem = menuItemRepository.save(menuItem);

        // 8. Return 201 Created
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedItem);
    }
}