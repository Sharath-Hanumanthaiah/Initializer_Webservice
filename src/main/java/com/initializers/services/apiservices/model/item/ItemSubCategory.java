package com.initializers.services.apiservices.model.item;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "item_sub_category")
public class ItemSubCategory {
	
	@Transient
    public static final String SEQUENCE_NAME = "item_subcategory_sequence";
 
	@Id
	private Long id;
	private Long categoryId;
	private String name;
	private String description;
	private Long offer;
	private String imageLink;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getOffer() {
		return offer;
	}
	public void setOffer(Long offer) {
		this.offer = offer;
	}
}
