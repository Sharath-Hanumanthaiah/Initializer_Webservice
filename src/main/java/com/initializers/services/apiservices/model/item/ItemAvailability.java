package com.initializers.services.apiservices.model.item;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "item_availability")
public class ItemAvailability {
	
	@Transient
    public static final String SEQUENCE_NAME = "item_availability_sequence";
 
	
	@Id
	private Long id;
	private Long itemId;
	private Float actualPrice;
	private Long discount;
	private Float discountPrice;
	private Float value;
	private String unit;
	private String available;
	private String state;
	
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
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	public Float getActualPrice() {
		return actualPrice;
	}
	public void setActualPrice(Float actualPrice) {
		this.actualPrice = actualPrice;
	}
	public Long getDiscount() {
		return discount;
	}
	public void setDiscount(Long discount) {
		this.discount = discount;
	}
	public Float getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(Float discountPrice) {
		this.discountPrice = discountPrice;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
