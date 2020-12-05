package com.initializers.services.apiservices.model.item;

import java.util.List;

public class ItemDetailsList {
	private Long id;
	private String categoryId;
	private String subCategoryId;
	private String name;
	private ItemDescription description;
	private List<String> imageLinks;
	private String status;
	private String categoryName;
	private String subCategoryName;
	
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
	public String getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(String subCategoryId) {
		this.subCategoryId = subCategoryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ItemDescription getDescription() {
		return description;
	}
	public void setDescription(ItemDescription description) {
		this.description = description;
	}
	public List<String> getImageLinks() {
		return imageLinks;
	}
	public void setImageLinks(List<String> imageLinks) {
		this.imageLinks = imageLinks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getSubCategoryName() {
		return subCategoryName;
	}
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}
	
	
}
