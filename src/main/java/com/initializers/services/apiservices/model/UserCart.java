package com.initializers.services.apiservices.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_cart")
public class UserCart {

	@Transient
    public static final String SEQUENCE_NAME = "users_cart_sequence";
	
	@Id
    private CompositeKey id;
	
	private Long quantity;
	
    public static class CompositeKey implements Serializable {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Long userId;
    	private Long itemId;
    	private Long availabilityId;
        
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
    }
	
	public CompositeKey getId() {
		return id;
	}
	public void setId(CompositeKey id) {
		this.id = id;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	
	
}
