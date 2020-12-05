package com.initializers.services.apiservices.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.initializers.services.apiservices.model.item.ItemSubCategory;
import com.initializers.services.apiservices.others.Suggestions;

@Service
public interface ItemSubCategoryService {

	Object addItemSubCategory(ItemSubCategory itemSubCategory, MultipartFile image);
	
	ItemSubCategory addItemSubCategoryAdmin(ItemSubCategory itemSubCategory);
	
	List<Object> getItemSubCategory(String[] filter, Suggestions suggestions);
	
	ItemSubCategory getItemSubCategoryById(Long id);
	
	String getItemSubCategoryName(Long id);
	
	ItemSubCategory getMaxOfferItemSubCategoryByCategoryId(Long categoryId);

	Map<String, Object> deleteItemSubCategoryByCategoryId(Long categoryId);
	
	Map<String, Object> deleteItemSubCategoryById(Long subCategoryId);
	
	Map<String, Object> updateSubCategory(ItemSubCategory itemSubCategory, MultipartFile image);
	
	ItemSubCategory updateSubCategoryAdmin(ItemSubCategory itemSubCategory);
	
	Map<String, Object> updateItemSubCategoryImage(MultipartFile image, Long subCategoryId);
	
	List<ItemSubCategory> getItemSubCategoryByCategoryId(Long categoryId, Pageable pageable);
	
	Map<String, Object> updateItemSubCategoryDescription(ItemSubCategory itemSubCategory, Long subCategoryId);
	
	boolean checkItemSubCategory(Long itemSubCategoryId);
	
	void updateItemSubCategoryOffer(Long itemId, Long offer);
}
