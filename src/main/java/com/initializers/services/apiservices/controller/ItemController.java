package com.initializers.services.apiservices.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.initializers.services.apiservices.model.ItemDetailsSearch;
import com.initializers.services.apiservices.model.TopDeals;
import com.initializers.services.apiservices.model.item.ItemAvailability;
import com.initializers.services.apiservices.model.item.ItemCategory;
import com.initializers.services.apiservices.model.item.ItemDetails;
import com.initializers.services.apiservices.model.item.ItemDetailsTemp;
import com.initializers.services.apiservices.model.item.ItemListTemp;
import com.initializers.services.apiservices.model.item.ItemSubCategory;
import com.initializers.services.apiservices.repo.ItemCategoryRepo;
import com.initializers.services.apiservices.service.ItemAvailabilityService;
import com.initializers.services.apiservices.service.ItemCategoryService;
import com.initializers.services.apiservices.service.ItemDetailsService;
import com.initializers.services.apiservices.service.ItemSubCategoryService;
import com.initializers.services.apiservices.service.TopDealsService;

@RestController()
@RequestMapping("/item")
@CrossOrigin
public class ItemController {

	@Autowired
	private ItemCategoryService itemCategoryService;
	@Autowired
	private ItemSubCategoryService itemSubCategoryService;
	@Autowired
	private ItemDetailsService itemDetailsService;
	@Autowired
	private ItemAvailabilityService itemAvailabilityService;
	@Autowired
	private TopDealsService topDealsService;
	@Autowired
	private ItemCategoryRepo itemCategoryRepo;
	
	
	// category
	@GetMapping("/category")
	public Object getItemCategory(Pageable pagable) {
		return itemCategoryService.getItemCategory(pagable);
	}
	
	@GetMapping("/category/{categoryId}")
	public ItemCategory getItemCategoryById(@PathVariable("categoryId") Long id) {
		return itemCategoryService.getItemCategoryById(id);
	}

	@PostMapping("/category")
	public Object addCategory(@RequestParam("details") ItemCategory itemCategory,
			@RequestPart(required = false) MultipartFile image) {
		return itemCategoryService.addItemCategory(itemCategory, image);
	}

	@DeleteMapping("/category/{categoryId}")
	public Map<String, Object> deleteCategoryById(@PathVariable Long categoryId) {
		return itemCategoryService.deleteItemCategoryById(categoryId);
	}

	@PutMapping("/category/image/{categoryId}")
	public Map<String, Object> updateCategoryImage(@RequestPart("image") MultipartFile image,
			@PathVariable("categoryId") Long categoryId) {
		return itemCategoryService.updateItemCategoryImage(image, categoryId);
	}

	@PutMapping("/category/description/{categoryId}")
	public Map<String, Object> updateCategoryDescription(@RequestBody ItemCategory itemCategory,
			@PathVariable("categoryId") Long categoryId) {
		return itemCategoryService.updateItemCategoryDescription(itemCategory, categoryId);
	}

	@PutMapping("/category")
	public Map<String, Object> updateCategory(@RequestPart("details") ItemCategory itemCategory,
			@RequestPart("image") MultipartFile image) {
		return itemCategoryService.updateCategory(itemCategory, image);
	}

	
	//*****************************************************************************************
	// sub category
	//*****************************************************************************************
	@GetMapping("/sub-category/category/{categoryId}")
	public List<ItemSubCategory> getItemSubCategoryByCategoryId(@PathVariable("categoryId") Long categoryId, Pageable pageable) {
		return itemSubCategoryService.getItemSubCategoryByCategoryId(categoryId, pageable);
	}
	@GetMapping("/sub-category/{subCategoryId}")
	public ItemSubCategory getItemSubCategoryById(@PathVariable("subCategoryId") Long id) {
		return itemSubCategoryService.getItemSubCategoryById(id);
	}
	@PostMapping("/sub-category")
	public Object addSubCategory(@RequestParam("details") ItemSubCategory itemSubCategory,
			@RequestPart("image") MultipartFile image) {
		return itemSubCategoryService.addItemSubCategory(itemSubCategory, image);
	}

	@DeleteMapping("/sub-category/{subCategoryId}")
	public Map<String, Object> deleteSubCategoryByCategory(@PathVariable Long subCategoryId) {
		return itemSubCategoryService.deleteItemSubCategoryById(subCategoryId);
	}

	@PutMapping("/sub-category/image/{subCategoryId}")
	public Map<String, Object> updateSubCategoryImage(@RequestPart("file") MultipartFile image,
			@PathVariable("subCategoryId") Long subCategoryId) {
		return itemSubCategoryService.updateItemSubCategoryImage(image, subCategoryId);
	}

	@PutMapping("/sub-category/description/{subCategoryId}")
	public Map<String, Object> updateSubCategoryDescription(@RequestBody ItemSubCategory itemSubCategory,
			@PathVariable("subCategoryId") Long subCategoryId) {
		return itemSubCategoryService.updateItemSubCategoryDescription(itemSubCategory, subCategoryId);
	}

	@PutMapping("/sub-category")
	public Map<String, Object> updateSubCategory(@RequestPart("details") ItemSubCategory itemSubCategory,
			@RequestPart("image") MultipartFile image) {
		return itemSubCategoryService.updateSubCategory(itemSubCategory, image);
	}
	//**********************************************************************************************
	// itemDetails
	//**********************************************************************************************
	@GetMapping("/{itemId}")
	public ItemDetailsTemp getItemDetails(@PathVariable Long itemId) {
		return itemDetailsService.getItemDetails(itemId);
	}
	@GetMapping("/byCategory/{categoryID}")
	public Object getItemDetailsByCategory(@PathVariable Long categoryID, Pageable pageable) {
		return itemDetailsService.getItemDetailsByCategory(categoryID, pageable);
	}
	@GetMapping("/bySubCategory/{subCategoryID}")
	public List<ItemListTemp> getItemDetailsBySubCategory(@PathVariable Long subCategoryID, Pageable pageable) {
		return itemDetailsService.getItemDetailsBySubCategory(subCategoryID, pageable);
	}
	@PostMapping("/")
	public Object addItemDetails(@RequestParam("details") ItemDetails itemDetails,
			@RequestPart("images") List<MultipartFile> images) {
		return itemDetailsService.addItemDetails(itemDetails, images); 
	}
	@PutMapping("/")
	public Object updateItemDetails(@RequestParam("details") ItemDetails itemDetails,
			@RequestPart("images") List<MultipartFile> images) {
		return itemDetailsService.updateItemDetails(itemDetails, images);
	}
	@DeleteMapping("/{itemId}")
	public Object deleteItemDetails(@PathVariable Long itemId) {
		return itemDetailsService.deleteItemDetails(itemId);
	}
	@GetMapping("/search/{itemName}")
	public List<ItemDetailsSearch> searchItem(@PathVariable String itemName) {
		return itemDetailsService.searchItemDetails(itemName);
	}
	
	@GetMapping("/test")
	public Object test(Pageable pageable) {
//		@RequestParam(defaultValue = "0") Integer pageNo, 
//		itemCategoryService.updateItemCategoryOffer(14L);
		return itemCategoryRepo.findAll(pageable);
//		itemCategoryRepo.findItemCategoryJoinSubCategory();
//		 itemCategoryService.getItemCategorySubCategory();
//		 return "test";
	} 
	//*************************************************************************
	//Item Availability 
	//*************************************************************************
	@GetMapping("/availability/{itemId}")
	public List<ItemAvailability> getAvailability(@PathVariable Long itemId) {
		return itemAvailabilityService.getAvailabilityByItemId(itemId);
	}
	@PostMapping("/availability")
	public ItemAvailability addAvailability(@RequestBody ItemAvailability itemAvailability) {
		return itemAvailabilityService.addAvailability(itemAvailability);
	}
	//*************************************************************************
	//top deals
	//*************************************************************************
	@GetMapping("/top-deals")
	public List<TopDeals> getTopDeals() {
		return topDealsService.getTopDeals();
	}
	@PostMapping("/top-deals")
	public TopDeals addTopDeals(@RequestBody TopDeals topDeals) {
		return topDealsService.addNewTopDeals(topDeals);
	}
}
