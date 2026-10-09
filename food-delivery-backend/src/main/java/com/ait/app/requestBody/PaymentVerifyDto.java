package com.ait.app.requestBody;

public class PaymentVerifyDto {
	
	private int orderId;
	private String razorpayOrderId;
	private String razorpaypaymentId;
	private String razorpaySignature;
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}
	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}
	public String getRazorpaypaymentId() {
		return razorpaypaymentId;
	}
	public void setRazorpaypaymentId(String razorpaypaymentId) {
		this.razorpaypaymentId = razorpaypaymentId;
	}
	public String getRazorpaySignature() {
		return razorpaySignature;
	}
	public void setRazorpaySignature(String razorpaySignature) {
		this.razorpaySignature = razorpaySignature;
	}

}
