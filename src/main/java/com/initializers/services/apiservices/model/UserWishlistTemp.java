package com.initializers.services.apiservices.model;

import org.springframework.data.annotation.Id;

public class UserWishlistTemp {
	@Id
	private Long userID;
	private Long itemsId;
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public Long getItemsId() {
		return itemsId;
	}
	public void setItemsId(Long itemsId) {
		this.itemsId = itemsId;
	}
	
	
}
