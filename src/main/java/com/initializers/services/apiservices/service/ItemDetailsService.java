package com.initializers.services.apiservices.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.initializers.services.apiservices.model.ItemDetailsSearch;
import com.initializers.services.apiservices.model.item.ItemDetails;
import com.initializers.services.apiservices.model.item.ItemDetailsTemp;
import com.initializers.services.apiservices.model.item.ItemListTemp;
import com.initializers.services.apiservices.others.Suggestions;

@Service
public interface ItemDetailsService {
	
	Object addItemDetails(ItemDetails itemDetails, List<MultipartFile> images);
	
	Object addItemDetailsAdmin(ItemDetails itemDetails);
	
	ItemDetailsTemp getItemDetails(Long id);
	
	Object getAdminItemDetails(Long id);
	
	List<Object> getItemDetailsList(String[] filter, Suggestions suggestions);
	
	Object updateItemDetails(ItemDetails itemDetails, List<MultipartFile> images);
	
	Object updateItemDetailsAdmin(ItemDetails itemDetails);
	
	Object getItemDetailsByCategory(Long categoryID, Pageable pageable);
	
	List<ItemListTemp> getItemDetailsBySubCategory(Long subCategoryID, Pageable pageable);
	
	List<ItemDetailsSearch> searchItemDetails(String name);
	
	ItemListTemp getItemListById(Long id, Long... availabilityId);
	
	Object deleteItemDetails(Long id);
	
	String getItemNameById(Long id);
}
