package com.ait.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartitemid;
	private int quantity;
	private double unitprice;
	private double subtotal;
	@ManyToOne()
	@JoinColumn(name = "cart_id")
	private Cart cart;
	@ManyToOne
	@JoinColumn(name = "menu_item_id")
	private MenuItem menuItem;
	
	public int getCartitemid() {
		return cartitemid;
	}
	public void setCartitemid(int cartitemid) {
		this.cartitemid = cartitemid;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(double unitprice) {
		this.unitprice = unitprice;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public Cart getCart() {
		return cart;
	}
	public void setCart(Cart cart) {
		this.cart = cart;
	}
	public MenuItem getMenuItem() {
		return menuItem;
	}
	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}
}
