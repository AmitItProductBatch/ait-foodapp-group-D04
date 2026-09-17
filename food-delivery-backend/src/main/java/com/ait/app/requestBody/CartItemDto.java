package com.ait.app.requestBody;

public class CartItemDto {
	private int cartId;
	private int MenuItemId;
	private int quantity;
	private double unitPrice;

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public int getMenuItemId() {
		return MenuItemId;
	}

	public void setMenuItemId(int menuItemId) {
		MenuItemId = menuItemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
