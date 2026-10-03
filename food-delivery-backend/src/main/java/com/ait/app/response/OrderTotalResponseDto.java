package com.ait.app.response;

import java.util.List;

public class OrderTotalResponseDto {
	
	private List<PriceCalculationResponse> items;
	private double itemSubtotal;
	private double taxAmount;
	private double deliveryFee;
	private double discountAmount;
    private double orderTotal;
    
    public OrderTotalResponseDto() {
	     super();
    }

	public List<PriceCalculationResponse> getItems() {
		return items;
	}

	public void setItems(List<PriceCalculationResponse> items) {
		this.items = items;
	}

	public double getItemSubtotal() {
		return itemSubtotal;
	}

	public void setItemSubtotal(double itemSubtotal) {
		this.itemSubtotal = itemSubtotal;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}
    
	

}
