package com.initializers.services.apiservices.model;

public class UserOrder {

	private Long userId;
	private Long itemId;
	private Long availabilityId;
	private Long quantity;
	private Float amount;

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getAvailabilityId() {
		return availabilityId;
	}
	public void setAvailabilityId(Long availabilityId) {
		this.availabilityId = availabilityId;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	
}
