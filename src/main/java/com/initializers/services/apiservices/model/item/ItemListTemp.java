package com.initializers.services.apiservices.model.item;

import java.util.List;

import org.springframework.data.annotation.Id;

public class ItemListTemp {

	@Id
	private Long id;
	private String name;
	private String imageLinks;
	private List<ItemAvailability> availablity;
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
	public String getImageLinks() {
		return imageLinks;
	}
	public void setImageLinks(String imageLinks) {
		this.imageLinks = imageLinks;
	}
	public List<ItemAvailability> getAvailablity() {
		return availablity;
	}
	public void setAvailablity(List<ItemAvailability> availablity) {
		this.availablity = availablity;
	}
}
