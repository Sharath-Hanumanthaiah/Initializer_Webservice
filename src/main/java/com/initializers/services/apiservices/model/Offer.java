package com.initializers.services.apiservices.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "offers")
public class Offer {
	
	@Id
	private Long id;
	private Long itemId;
	private Long discount;
	private Long discountPrice;
	private Date timeStamp;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getDiscount() {
		return discount;
	}
	public void setDiscount(Long discount) {
		this.discount = discount;
	}
	public Long getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(Long discountPrice) {
		this.discountPrice = discountPrice;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
}
