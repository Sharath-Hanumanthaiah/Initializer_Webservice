package com.initializers.services.apiservices.model.item;

import java.util.List;

import org.springframework.data.annotation.Id;

public class ItemDetailsTemp {

	@Id
	private Long id;
	private Long categoryId;
	private Long subCategoryId;
	private String name;
	private List<ItemAvailability> itemAvailabilities;
	private ItemDescription description;
	private List<String> imageLinks;
	
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
	public List<ItemAvailability> getItemAvailabilities() {
		return itemAvailabilities;
	}
	public void setItemAvailabilities(List<ItemAvailability> itemAvailabilities) {
		this.itemAvailabilities = itemAvailabilities;
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
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}
}
