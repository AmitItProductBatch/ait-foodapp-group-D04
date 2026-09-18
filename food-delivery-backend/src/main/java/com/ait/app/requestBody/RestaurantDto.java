package com.ait.app.requestBody;

import java.util.List;

import com.ait.app.model.Cuisine;
import com.ait.app.model.MenuItem;

public class RestaurantDto {

	private String name;
	private String address;
	private String contactNo;
	private int userId;
	private double rating;
	private List<Cuisine> cuisine;
	private boolean active;
	private boolean approved;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<Cuisine> getCuisine() {
		return cuisine;
	}

	public void setCuisine(List<Cuisine> cuisine) {
		this.cuisine = cuisine;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	

}