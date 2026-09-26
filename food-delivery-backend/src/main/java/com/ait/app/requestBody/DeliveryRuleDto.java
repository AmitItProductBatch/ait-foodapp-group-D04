package com.ait.app.requestBody;

public class DeliveryRuleDto {
	
	private double baseFee;

	private double perKmRate;

	private double maxDeliveryRadius;

	private double freeDeliveryThreshold;

	public double getBaseFee() {
		return baseFee;
	}

	public void setBaseFee(double baseFee) {
		this.baseFee = baseFee;
	}

	public double getPerKmRate() {
		return perKmRate;
	}

	public void setPerKmRate(double perKmRate) {
		this.perKmRate = perKmRate;
	}

	public double getMaxDeliveryRadius() {
		return maxDeliveryRadius;
	}

	public void setMaxDeliveryRadius(double maxDeliveryRadius) {
		this.maxDeliveryRadius = maxDeliveryRadius;
	}

	public double getFreeDeliveryThreshold() {
		return freeDeliveryThreshold;
	}

	public void setFreeDeliveryThreshold(double freeDeliveryThreshold) {
		this.freeDeliveryThreshold = freeDeliveryThreshold;
	}
	
	

}
