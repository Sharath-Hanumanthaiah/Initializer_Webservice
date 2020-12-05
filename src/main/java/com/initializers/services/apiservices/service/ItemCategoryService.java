package com.initializers.services.apiservices.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.initializers.services.apiservices.model.item.ItemCategory;
import com.initializers.services.apiservices.others.Suggestions;

@Service
public interface ItemCategoryService {
	
	Object addItemCategory(ItemCategory itemCategory, MultipartFile image);
	
	ItemCategory addItemCategoryAdmin(ItemCategory itemCategory);
	
	Object getItemCategory(Pageable pagable);
	
	Object getItemCategory(String[] filter, Suggestions suggestions);
	
	String getItemCategoryName(Long id);
	
	ItemCategory getItemCategoryById(Long id);
	
	ItemCategory updateCategoryAdmin(ItemCategory itemCategory);
	
	Map<String, Object> deleteItemCategoryById(Long categoryId);
	
	Map<String, Object> updateCategory(ItemCategory itemCategory, MultipartFile image);
	
	Map<String, Object> updateItemCategoryImage(MultipartFile image, Long categoryId);
	
	Map<String, Object> updateItemCategoryDescription(ItemCategory itemCategory, Long categoryId);
	
	boolean checkItemCategory(Long itemCategoryId);
	
	void updateItemCategoryOffer(Long itemId, Long offer);
	
	void getItemCategorySubCategory();
}
