package com.ait.app.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ait.app.Service.MenuItemService;
import com.ait.app.customExceptionHandler.RestaurantException;
import com.ait.app.model.MenuItem;
import com.ait.app.model.Restaurant;
import com.ait.app.model.User;
import com.ait.app.repository.MenuItemRepository;
import com.ait.app.repository.RestaurantRepo;
import com.ait.app.requestBody.MenuItemDto;
import com.ait.app.requestBody.PriceResponseDto;
import com.ait.app.response.MenuItemResponse;
import com.ait.app.response.MenuResponse;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepo restaurantRepo;

    @Override
    public ResponseEntity<MenuItem> addMenuItem(int restaurantId, MenuItemDto menuItemDto) {

        if (menuItemDto.getName() == null || menuItemDto.getName().trim().isEmpty()) {
            throw new RestaurantException("Name is required", HttpStatus.BAD_REQUEST);
        }

        if (menuItemDto.getDescription() == null
                || menuItemDto.getDescription().trim().isEmpty()) {
            throw new RestaurantException("Description is required", HttpStatus.BAD_REQUEST);
        }

        if (menuItemDto.getPrice() <= 0) {
            throw new RestaurantException("Price must be greater than 0", HttpStatus.BAD_REQUEST);
        }

        if (menuItemDto.getCategory() == null
                || menuItemDto.getCategory().trim().isEmpty()) {
            throw new RestaurantException("Category is required", HttpStatus.BAD_REQUEST);
        }

        Optional<Restaurant> optionalRestaurant = restaurantRepo.findById(restaurantId);

        if (optionalRestaurant.isEmpty()) {
            throw new RestaurantException("Restaurant not found", HttpStatus.NOT_FOUND);
        }

        Restaurant restaurant = optionalRestaurant.get();

        User user = restaurant.getUser();

        if (user == null) {
            throw new RestaurantException(
                    "No user is linked with this restaurant",
                    HttpStatus.UNAUTHORIZED);
        }

        if (menuItemRepository.existsByRestaurantIdAndName(
                restaurantId, menuItemDto.getName())) {

            throw new RestaurantException(
                    "Menu item with this name already exists for this restaurant",
                    HttpStatus.CONFLICT);
        }

        MenuItem menuItem = new MenuItem();

        menuItem.setName(menuItemDto.getName());
        menuItem.setDescription(menuItemDto.getDescription());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setAvailability(menuItemDto.isAvailability());
        menuItem.setCategory(menuItemDto.getCategory());
        menuItem.setRestaurant(restaurant);

        MenuItem savedItem = menuItemRepository.save(menuItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    @Override
    public ResponseEntity updateMenuItem(int itemId, MenuItemDto menuItemDto) {

        Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(itemId);

        if (optionalMenuItem.isEmpty()) {
            throw new RestaurantException(
                    "Menu item not found",
                    HttpStatus.NOT_FOUND);
        }

        MenuItem menuItem = optionalMenuItem.get();

        if (menuItemDto.getPrice() > 0) {
            menuItem.setPrice(menuItemDto.getPrice());
        }

        if (menuItemDto.getDescription() != null) {
            menuItem.setDescription(menuItemDto.getDescription());
        }

        if (menuItemDto.isAvailability()) {
            menuItem.setAvailability(true);
        } else {
            menuItem.setAvailability(false);
        }

        MenuItem updatedItem = menuItemRepository.save(menuItem);

        return ResponseEntity.status(HttpStatus.OK).body(updatedItem);
    }

    @Override
    public ResponseEntity deleteMenuItem(int itemId, int userId) {

        Optional<MenuItem> optional = menuItemRepository.findById(itemId);

        if (optional.isEmpty()) {
            throw new RestaurantException(
                    "Menu item not found",
                    HttpStatus.NOT_FOUND);
        }

        MenuItem item = optional.get();

        Restaurant restaurant = item.getRestaurant();

        if (restaurant.getUser().getId() != userId) {
            throw new RestaurantException(
                    "You are not authorized to delete this item",
                    HttpStatus.UNAUTHORIZED);
        }

        item.setActive(false);
        menuItemRepository.save(item);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity getItemDetails(int itemId) {

        Optional<MenuItem> menuItem = menuItemRepository.findById(itemId);

        if (menuItem.isEmpty()) {
            throw new RestaurantException(
                    "this item is not available ",
                    HttpStatus.NOT_FOUND);
        }

        if (!menuItem.get().isAvailability()) {
            throw new RestaurantException(
                    "this item is not available right now ",
                    HttpStatus.FOUND);
        }

        try {
            PriceResponseDto priceDto = new PriceResponseDto();

            priceDto.setItemId(itemId);
            priceDto.setPrice(menuItem.get().getPrice());

            String description = menuItem.get().getDescription();

            Map priceResponse = new HashMap();
            priceResponse.put("priceDto", priceDto);
            priceResponse.put("description", description);

            return ResponseEntity.status(HttpStatus.FOUND).body(priceResponse);

        } catch (Exception e) {
            throw new RestaurantException(
                    "There is an internal issue for getting item price ",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<MenuResponse> getRestaurantMenu(int restaurantId) {

        Optional<Restaurant> optionalRestaurant = restaurantRepo.findById(restaurantId);

        if (optionalRestaurant.isEmpty()) {
            throw new RestaurantException(
                    "Restaurant not found",
                    HttpStatus.NOT_FOUND);
        }

        List<MenuItem> menuItems =
                menuItemRepository.findByRestaurantIdAndAvailabilityTrue(restaurantId);

        Map<String, List<MenuItemResponse>> groupedMenu =
                new LinkedHashMap<String, List<MenuItemResponse>>();

        for (MenuItem menuItem : menuItems) {

            MenuItemResponse itemResponse = new MenuItemResponse();

            itemResponse.setName(menuItem.getName());
            itemResponse.setDescription(menuItem.getDescription());
            itemResponse.setPrice(menuItem.getPrice());

            String category = menuItem.getCategory();

            if (!groupedMenu.containsKey(category)) {
                groupedMenu.put(category, new ArrayList<MenuItemResponse>());
            }

            groupedMenu.get(category).add(itemResponse);
        }

        List<MenuResponse> menuResponse = new ArrayList<MenuResponse>();

        for (Map.Entry<String, List<MenuItemResponse>> entry
                : groupedMenu.entrySet()) {

            MenuResponse response = new MenuResponse();

            response.setCategory(entry.getKey());
            response.setItems(entry.getValue());

            menuResponse.add(response);
        }

        return menuResponse;
    }
}