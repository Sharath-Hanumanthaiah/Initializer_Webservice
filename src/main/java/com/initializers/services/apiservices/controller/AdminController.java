package com.initializers.services.apiservices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.initializers.services.apiservices.model.UserOrderSet;
import com.initializers.services.apiservices.model.item.ItemAvailability;
import com.initializers.services.apiservices.model.item.ItemCategory;
import com.initializers.services.apiservices.model.item.ItemDetails;
import com.initializers.services.apiservices.model.item.ItemSubCategory;
import com.initializers.services.apiservices.others.Suggestions;
import com.initializers.services.apiservices.service.ImageService;
import com.initializers.services.apiservices.service.ItemAvailabilityService;
import com.initializers.services.apiservices.service.ItemCategoryService;
import com.initializers.services.apiservices.service.ItemDetailsService;
import com.initializers.services.apiservices.service.ItemSubCategoryService;
import com.initializers.services.apiservices.service.UserOrderService;

@RestController()
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {
	
	@Autowired
	private ItemDetailsService itemDetailsService;
	@Autowired
	private ItemCategoryService itemCategoryService;
	@Autowired
	private ItemSubCategoryService itemSubCategoryService;
	@Autowired
	private ItemAvailabilityService itemAvailabilityService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private UserOrderService userOrderService;
	
//***********************************************************************************
//	Item Details 
//***********************************************************************************
	@GetMapping("/itemDetails")
	public List<Object> getItemDetails(@RequestParam(required=false) String[] filter,
			Suggestions suggestions) {
		return itemDetailsService.getItemDetailsList(filter, suggestions);
	}
	@GetMapping("/itemDetails/{itemId}")
	public Object getItemDetailsById(@PathVariable Long itemId) {
		return itemDetailsService.getAdminItemDetails(itemId);
	}
	@PostMapping("/itemDetails")
	public Object addItemDetails(@RequestParam ItemDetails itemDetails) {
		return itemDetailsService.addItemDetailsAdmin(itemDetails);
	}
	@PutMapping("/itemDetails")
	public Object updateItemDetails(@RequestParam ItemDetails itemDetails) {
		return itemDetailsService.updateItemDetailsAdmin(itemDetails);
	}

//***********************************************************************************
//	Item Availability 	
//***********************************************************************************
	@GetMapping("/itemAvailability/{availabilityId}")
	public Object getItemAvailability(@PathVariable Long availabilityId) {
		return itemAvailabilityService.getAvailabilityByIdAdmin(availabilityId);
	}
	@PutMapping("/itemAvailability")
	public Object addItemAvailability(@RequestParam ItemAvailability itemAvailability) {
		return itemAvailabilityService.updateAvailability(itemAvailability);
	}
	@PostMapping("/itemAvailability")
	public Object updateItemAvailability(@RequestParam ItemAvailability itemAvailability) {
		return itemAvailabilityService.addAvailability(itemAvailability);
//		return itemDetailsService.updateItemDetailsAdmin(itemDetails);
	}
	

//***********************************************************************************
//	Item Category	
//***********************************************************************************
	@GetMapping("/itemCategory")
	public Object getItemCategory(@RequestParam(required=false) String[] filter,
			Suggestions suggestions) {
		return itemCategoryService.getItemCategory(filter, suggestions);
	}
	@GetMapping("/itemCategory/{categoryId}")
	public Object getItemCategoryById(@PathVariable Long categoryId) {
		return itemCategoryService.getItemCategoryById(categoryId);
	}
	@PostMapping("/itemCategory")
	public Object addItemCategory(@RequestParam ItemCategory itemCategory) {
		return itemCategoryService.addItemCategoryAdmin(itemCategory);
	}
	@PutMapping("/itemCategory")
	public Object updateItemCategory(@RequestParam ItemCategory itemCategory) {
		return itemCategoryService.updateCategoryAdmin(itemCategory);
	}
	@DeleteMapping("/itemCategory/{categoryId}")
	public Object deleteCategoryById(@PathVariable Long categoryId) {
		return itemCategoryService.deleteItemCategoryById(categoryId);
	}

//***********************************************************************************
//	Item Sub Category	
//***********************************************************************************
	@GetMapping("/itemSubcategory")
	public List<Object> getItemSubcategory(@RequestParam(required=false) String[] filter,
			Suggestions suggestions) {
		return itemSubCategoryService.getItemSubCategory(filter, suggestions);
	}
	@GetMapping("/itemSubcategory/{subCategoryId}")
	public Object getItemSubCategoryById(@PathVariable Long subCategoryId) {
		return itemSubCategoryService.getItemSubCategoryById(subCategoryId);
	}
	@PostMapping("/itemSubcategory")
	public Object addItemSubCategory(@RequestParam ItemSubCategory itemSubcategory) {
		return itemSubCategoryService.addItemSubCategoryAdmin(itemSubcategory);
	}
	@PutMapping("/itemSubcategory")
	public Object updateItemSubCategory(@RequestParam ItemSubCategory itemSubcategory) {
		return itemSubCategoryService.updateSubCategoryAdmin(itemSubcategory);
	}
	@DeleteMapping("/itemSubcategory/{subCategoryId}")
	public Object deleteSubCategoryById(@PathVariable Long subCategoryId) {
		return itemSubCategoryService.deleteItemSubCategoryById(subCategoryId);
	}
	
//***********************************************************************************
//	Image Service	
//***********************************************************************************
	@PostMapping("/imageUpload")
	public Object uploadImage(@RequestPart("images") List<MultipartFile> images,
			@RequestPart("type") String type, @RequestPart("id") String id) {
		return imageService.addNewImage(images, type, id);
	}
	
	@PutMapping("/imageUpload")
	public Object updateImage(@RequestPart("images") String images,
			@RequestPart("type") String type, @RequestPart("id") String id) {
		//images should be the one that need to be deleted
		return imageService.updateImage(images, type, id);
	}

//***********************************************************************************
//	UserOrder
//***********************************************************************************
	@GetMapping("/userOrder")
	public Object getUserOrder(@RequestParam(required=false) String[] filter) {
		return userOrderService.getAllUserOrderAdmin(filter);
	}
	@GetMapping("/userOrder/{userOrderId}")
	public Object getUserOrderById(@PathVariable Long userOrderId) {
		return userOrderService.getUserOrderAdmin(userOrderId);
	}
	@PutMapping("/userOrder")
	public Object updateItemSubCategory(@RequestParam UserOrderSet userOrder) {
		return userOrderService.updateUserOrderSet(userOrder);
	}
}
