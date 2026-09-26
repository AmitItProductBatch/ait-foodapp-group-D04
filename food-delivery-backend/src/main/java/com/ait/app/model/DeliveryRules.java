package com.ait.app.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DeliveryRules {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    private double baseFee;

    private double perKmRate;

    private double maxDeliveryRadius;

    private double freeDeliveryThreshold;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
    

}
