package com.ait.app.response;

import java.util.List;

public class MenuResponse {

    private String category;
    private List<MenuItemResponse> items;

    public MenuResponse() {
    }

    public MenuResponse(String category, List<MenuItemResponse> items) {
        this.category = category;
        this.items = items;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<MenuItemResponse> getItems() {
        return items;
    }

    public void setItems(List<MenuItemResponse> items) {
        this.items = items;
    }
}