package com.initializers.services.apiservices.model.item;

public class ItemSubCategoryList {
	private Long id;
	private String categoryId;
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
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	
	
}
