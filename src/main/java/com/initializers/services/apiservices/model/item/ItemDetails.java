package com.initializers.services.apiservices.model.item;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "item_details")
public class ItemDetails {

	
	@Transient
    public static final String SEQUENCE_NAME = "item_details";
	
	@Id
	private Long id;
	private Long categoryId;
	private Long subCategoryId;
	private String name;
	private ItemDescription description;
	private List<String> imageLinks;
	private String status;
	
	
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
	public Long getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(Long subCategoryId) {
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
}
