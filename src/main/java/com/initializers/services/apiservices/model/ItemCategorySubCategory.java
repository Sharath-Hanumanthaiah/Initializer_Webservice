package com.initializers.services.apiservices.model;

import java.util.List;

public class ItemCategorySubCategory {
	
	private Long id;
	private String name;
	private String description;
	private Long offer;
	private String imageLink;
	private List<Long> subCategoryID;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public List<Long> getSubCategoryID() {
		return subCategoryID;
	}
	public void setSubCategoryID(List<Long> subCategoryID) {
		this.subCategoryID = subCategoryID;
	}
}
