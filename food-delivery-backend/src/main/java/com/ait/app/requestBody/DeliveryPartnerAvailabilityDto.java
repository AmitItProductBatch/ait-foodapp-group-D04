package com.ait.app.requestBody;

public class DeliveryPartnerAvailabilityDto {

	private Integer deliveryPartnerId;
	private String availabilityStatus;

	public Integer getDeliveryPartnerId() {
		return deliveryPartnerId;
	}

	public void setDeliveryPartnerId(Integer deliveryPartnerId) {
		this.deliveryPartnerId = deliveryPartnerId;
	}

	public String getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}
}
