package com.initializers.services.apiservices.model;

public class OrderStatus {

	public boolean confirmed = false;
	public boolean delivered = false;
	
	public boolean getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	public boolean getDelivered() {
		return delivered;
	}
	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}
	
	
}
