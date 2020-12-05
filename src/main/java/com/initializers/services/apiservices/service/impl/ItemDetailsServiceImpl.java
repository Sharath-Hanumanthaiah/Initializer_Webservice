package com.initializers.services.apiservices.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.initializers.services.apiservices.exception.CategoryNotFoundException;
import com.initializers.services.apiservices.exception.ItemNotFoundException;
import com.initializers.services.apiservices.exception.RequiredValueMissingException;
import com.initializers.services.apiservices.exception.SubCategoryNotFoundException;
import com.initializers.services.apiservices.model.CloudinaryConfig;
import com.initializers.services.apiservices.model.ItemDetailsSearch;
import com.initializers.services.apiservices.model.item.ItemAvailability;
import com.initializers.services.apiservices.model.item.ItemDescription;
import com.initializers.services.apiservices.model.item.ItemDetails;
import com.initializers.services.apiservices.model.item.ItemDetailsList;
import com.initializers.services.apiservices.model.item.ItemDetailsTemp;
import com.initializers.services.apiservices.model.item.ItemListTemp;
import com.initializers.services.apiservices.model.item.ItemSubCategory;
import com.initializers.services.apiservices.others.FilterAction;
import com.initializers.services.apiservices.others.FilterValue;
import com.initializers.services.apiservices.others.Suggestions;
import com.initializers.services.apiservices.repo.ItemDetailsRepo;
import com.initializers.services.apiservices.service.ItemAvailabilityService;
import com.initializers.services.apiservices.service.ItemCategoryService;
import com.initializers.services.apiservices.service.ItemDetailsService;
import com.initializers.services.apiservices.service.ItemSubCategoryService;

@Service
public class ItemDetailsServiceImpl implements ItemDetailsService {

	@Autowired
	private CloudinaryConfig cloudinaryConfig;
	@Autowired
	private ItemCategoryService itemCategoryService;
	@Autowired
	private ItemSubCategoryService itemSubCategoryService;
	@Autowired
	private ItemDetailsRepo itemDetailsRepo;
	@Autowired
	private ItemAvailabilityService itemAvailabilityService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public Object addItemDetails(ItemDetails itemDetails, List<MultipartFile> images) {
		Map<String, Object> returnVal = new HashMap<String, Object>();
		List<String> imageLinks = new ArrayList<String>();

		if (itemDetails.getCategoryId() == null | !itemCategoryService.checkItemCategory(itemDetails.getCategoryId())) {
			returnVal.put("Error", "category Id doesn't exist");
			return returnVal;
		}
		if (itemDetails.getSubCategoryId() != null
				& !itemSubCategoryService.checkItemSubCategory(itemDetails.getSubCategoryId())) {
			returnVal.put("Error", "sub category id doesn't exist");
			return returnVal;
		}

		for (MultipartFile image : images) {
			try {
				String imageUrl = cloudinaryConfig.addImage(image);
				if (imageUrl != null) {
					imageLinks.add(imageUrl);
				}
			} catch (IOException e1) {
				returnVal.put("Error", e1);
				e1.printStackTrace();
				return returnVal;
			}

		}
		itemDetails.setImageLinks(imageLinks);

		itemDetails.setStatus("Active");

		return itemDetailsRepo.save(itemDetails);
	}
	
	@Override
	public Object getAdminItemDetails(Long id) {
		List<AggregationOperation> list = new ArrayList<AggregationOperation>();
		ItemDetailsList itemDetails = new ItemDetailsList();
		Map<String, Object> resultVal = new HashMap<String, Object>();
//		if (itemDetails == null) {
//			throw new ItemNotFoundException();
//		} else {
			list.add(LookupOperation.newLookup().from("item_sub_category").localField("subCategoryId")
					.foreignField("_id").as("join_subcat"));
			list.add(LookupOperation.newLookup().from("item_category").localField("categoryId")
					.foreignField("_id").as("join_cat"));
			list.add(Aggregation.match( Criteria.where("_id").
					is(id)));
			list.add(Aggregation.project(Fields.fields("_id","name","subCategoryId",
					"imageLinks","description","itemAvailabilities","status","categoryId").
					and(Fields.field("subCategoryName", "join_subcat.name")).
					and(Fields.field("categoryName", "join_cat.name"))));
			Aggregation aggregation = Aggregation.newAggregation(list);
			List<ItemDetailsList> results = mongoTemplate.aggregate(aggregation, "item_details", ItemDetailsList.class)
					.getMappedResults();
			if(results.size() > 0) {
				 itemDetails = results.get(0);
				 List<ItemAvailability> itemAvailability = itemAvailabilityService.getAvailabilityByItemId(id, 'N');
				 List<String> images = itemDetails.getImageLinks();
				 resultVal.put("id", itemDetails.getId());
				 resultVal.put("categoryId", itemDetails.getCategoryId());
				 resultVal.put("subCategoryId", itemDetails.getSubCategoryId());
				 resultVal.put("categoryName", itemDetails.getCategoryName());
				 resultVal.put("subCategoryName", itemDetails.getSubCategoryName());
				 resultVal.put("name", itemDetails.getName());
				 resultVal.put("imageLinks", images);
				 resultVal.put("description", itemDetails.getDescription());
				 resultVal.put("itemAvailabilities", itemAvailability);
				 resultVal.put("status", itemDetails.getStatus());
				 resultVal.put("state", itemDetails.getStatus().equals("Active")? "Success": "Error");
				 resultVal.put("defaultImage", images == null || images.size() <= 0? "": images.get(0));
			}
			return resultVal;
//		}
	}
	@Override
	public ItemDetailsTemp getItemDetails(Long id) {
		ItemDetails itemDetails = itemDetailsRepo.findFirstId(id);
		if (itemDetails == null) {
			throw new ItemNotFoundException();
		} else {
			ItemDetailsTemp itemDetailsTemp = new ItemDetailsTemp();
			List<ItemAvailability> itemAvailability = itemAvailabilityService.getAvailabilityByItemId(id);
			itemDetailsTemp.setId(itemDetails.getId());
			itemDetailsTemp.setCategoryId(itemDetails.getCategoryId());
			itemDetailsTemp.setSubCategoryId(itemDetails.getSubCategoryId());
			itemDetailsTemp.setName(itemDetails.getName());
			itemDetailsTemp.setImageLinks(itemDetails.getImageLinks());
			itemDetailsTemp.setDescription(itemDetails.getDescription());
			itemDetailsTemp.setItemAvailabilities(itemAvailability);
			return itemDetailsTemp;
		}
	}

	@Override
	public Object updateItemDetails(ItemDetails itemDetails, List<MultipartFile> images) {

		Map<String, Object> returnVal = new HashMap<String, Object>();
		ItemDetails dbItemDetails = itemDetailsRepo.findFirstById(itemDetails.getId());

		if (itemDetails.getCategoryId() == null | !itemCategoryService.checkItemCategory(itemDetails.getCategoryId())) {
			returnVal.put("Error", "category Id doesn't exist");
			return returnVal;
		} else
			dbItemDetails.setCategoryId(itemDetails.getCategoryId());
		if (itemDetails.getSubCategoryId() == null
				| !itemSubCategoryService.checkItemSubCategory(itemDetails.getSubCategoryId())) {
			returnVal.put("Error", "sub category id doesn't exist");
			return returnVal;
		} else
			dbItemDetails.setSubCategoryId(itemDetails.getSubCategoryId());

		for (MultipartFile image : images) {
			try {
				String imageUrl = cloudinaryConfig.addImage(image);
				if (imageUrl != null) {
					if(itemDetails.getImageLinks() == null) {
						List<String> imageList = new ArrayList<>();
						imageList.add(imageUrl);
						itemDetails.setImageLinks(imageList);
					}else {
						itemDetails.getImageLinks().add(imageUrl);
					}
				}
			} catch (IOException e1) {
				returnVal.put("Error", e1);
				e1.printStackTrace();
				return returnVal;
			}
		}
		dbItemDetails.setImageLinks(itemDetails.getImageLinks());
		if (itemDetails.getName() != null) {
			dbItemDetails.setName(itemDetails.getName());
		}
		if (itemDetails.getDescription() != null) {
			dbItemDetails.setDescription(itemDetails.getDescription());
		}
//		itemDetails.setStatus("Active");
		itemDetailsRepo.save(dbItemDetails);
		return dbItemDetails;
	}

	@Override
	public Object getItemDetailsByCategory(Long categoryID, Pageable pageable) {
//		Map<String, Object> returnVal = new HashMap<>();
		List<ItemSubCategory> itemSubCategory = itemSubCategoryService.getItemSubCategoryByCategoryId(categoryID, pageable);
		if (itemSubCategory != null && itemSubCategory.size() > 0) {
//			returnVal.put("returnType", "Sub Category");
//			returnVal.put("value", itemSubCategory);
			return itemSubCategory;
//			return returnVal;
		} else {
			List<ItemListTemp> itemList = new ArrayList<>();
			for (ItemDetails itemDetails : itemDetailsRepo.findByCategoryId(categoryID, pageable)) {
				if(itemDetails.getStatus().equals("Active")) {
					itemList.add(getItemListById(itemDetails.getId()));
				}
			}
			if(itemList.size() <= 0 && pageable.getPageNumber() == 0) {
				throw new ItemNotFoundException();
			}
			return itemList;
		}
	}

	@Override
	public List<ItemListTemp> getItemDetailsBySubCategory(Long subCategoryID, Pageable pageable) {
		List<ItemDetails> itemDetails = itemDetailsRepo.findBySubCategoryId(subCategoryID, pageable);
		if(itemDetails.size() <= 0 && pageable.getPageNumber() == 0) throw new ItemNotFoundException();
		
			List<ItemListTemp> itemListTemp = new ArrayList<>();
			for(ItemDetails item : itemDetails) {
				if(item.getStatus().equals("Active")) {
					itemListTemp.add(getItemListById(item.getId()));
				}
			}
			return itemListTemp;
	} 

	@Override
	public List<ItemDetailsSearch> searchItemDetails(String name) {
		// TODO Auto-generated method stub
		List<ItemDetails> itemList = itemDetailsRepo.findCategoryIdByNameRegex(name);
		List<ItemDetailsSearch> returnItemList = new ArrayList<>();
		if(itemList != null) {
			int counter = 1;
			for (Iterator<ItemDetails> iterator = itemList.iterator(); iterator.hasNext();) {
				if (counter > 10) {
					break;
				}
				ItemDetailsSearch tempItem = new ItemDetailsSearch();
				ItemDetails itemDetails = (ItemDetails) iterator.next();
				if(!itemDetails.getStatus().equals("Active")) {
					continue;
				}
				tempItem.setId(itemDetails.getId());
				tempItem.setName(itemDetails.getName());
				returnItemList.add(tempItem);
				counter++;
			}
		}
		return returnItemList;
	}

	@Override
	public ItemListTemp getItemListById(Long id, Long... availabilityId) {
		ItemListTemp itemList = new ItemListTemp();
		ItemDetails itemDetails = new ItemDetails();
		int availabilityLength = availabilityId.length;
		if(availabilityLength == 2) {
			itemDetails = itemDetailsRepo.findByIdForAllStatus(id);
		}else {
			itemDetails = itemDetailsRepo.findListByIdByStatus(id, "Active");
		}
		
		if(itemDetails != null) {
			itemList.setId(itemDetails.getId());
			itemList.setName(itemDetails.getName());
			if (itemDetails.getImageLinks() != null && itemDetails.getImageLinks().size() > 0) {
				itemList.setImageLinks(itemDetails.getImageLinks().get(0));
			}
			List<ItemAvailability> itemAvailability = new ArrayList<>();
			if(availabilityLength == 2) {
				itemAvailability = itemAvailabilityService.
						getAvailabilityByItemId(id, 'Y');
			}else {
				itemAvailability = itemAvailabilityService.
						getAvailabilityByItemId(id);
			}
			
			if(availabilityId.length > 0) {
				System.out.println(availabilityId.toString()); 
				for(ItemAvailability itemAvail : itemAvailability) {
					if(itemAvail.getId().equals(availabilityId[0])) {
						List<ItemAvailability> itemListTemp = new ArrayList<ItemAvailability>();
						itemListTemp.add(itemAvail);
						itemList.setAvailablity(itemListTemp);
					}
				}
			}else {
				itemList.setAvailablity(itemAvailability);
			}

			return itemList;
		}
			return null;
	}

	@Override
	public Object deleteItemDetails(Long id) {
		ItemDetails itemDetails = itemDetailsRepo.findFirstById(id);
		if(itemDetails == null) throw new ItemNotFoundException();
		itemDetails.setStatus("Inactive");
		itemDetailsRepo.save(itemDetails);
		return itemDetails.getId();
	}

	@Override
	public String getItemNameById(Long id) {
		ItemDetails itemDetails = itemDetailsRepo.findFirstNameById(id);
		return itemDetails.getName();
	}

	@Override
	public List<Object> getItemDetailsList(String[] filter, Suggestions suggestions) {
		List<AggregationOperation> list = new ArrayList<AggregationOperation>();
		if(suggestions.getKey() != null && suggestions.getValue() != null) {
			List<Object> suggestionsList = new ArrayList<>();
			list.add(Aggregation.project(Fields.fields().
					and(Fields.field("key", suggestions.getKey())).
					and(Fields.field("value", suggestions.getValue()))));
			Aggregation aggregation = Aggregation.newAggregation(list);
			suggestionsList = mongoTemplate.aggregate(aggregation, "item_details", Suggestions.class)
					.getMappedResults().stream().collect(Collectors.toList());
			return suggestionsList;
		}else {
			List<Object> itemDetailsList = new ArrayList<>();
			list.add(LookupOperation.newLookup().from("item_sub_category").localField("subCategoryId")
					.foreignField("_id").as("join_subcat"));
			list.add(LookupOperation.newLookup().from("item_category").localField("categoryId")
					.foreignField("_id").as("join_cat"));
			if(filter != null) {
				List<FilterValue> filterValues =  new FilterAction().getFilterValue(filter);
				for(FilterValue filterValue : filterValues) {
					List<Object> alteredFilterValue = new ArrayList<>();
					if(filterValue.getName() != null && filterValue.getOperator() != null 
							&& filterValue.getValue() != null) {
						switch (filterValue.getName()) {
						case "subCategoryId":
							alteredFilterValue = filterValue.getValue().stream().map(Long::parseLong).
							collect(Collectors.toList());
							break;
						case "categoryId":
							alteredFilterValue = filterValue.getValue().stream().map(Long::parseLong).
							collect(Collectors.toList());
							break;
						case "_id":
							alteredFilterValue = filterValue.getValue().stream().map(Long::parseLong).
							collect(Collectors.toList());
							break;
						default:
							alteredFilterValue = filterValue.getValue().stream().
							collect(Collectors.toList());
							break;
						}
						list.add(Aggregation.match( Criteria.where(filterValue.getName()).
								in(alteredFilterValue)));
					}
				}
			}
			list.add(Aggregation.project(Fields.fields("_id","name","imageLinks","status").
					and(Fields.field("subCategoryId", "join_subcat.name")).
					and(Fields.field("categoryId", "join_cat.name"))));
			Aggregation aggregation = Aggregation.newAggregation(list);
			List<ItemDetailsList> results = mongoTemplate.aggregate(aggregation, "item_details", ItemDetailsList.class)
					.getMappedResults();
			for(ItemDetailsList item : results) {
				Map<String, Object> resultVal = new HashMap<String, Object>();
				resultVal.put("id", item.getId());
				resultVal.put("name", item.getName());
				resultVal.put("categoryId", item.getCategoryId());
				resultVal.put("subCategoryId", item.getSubCategoryId());
				List<String> images = item.getImageLinks();
				resultVal.put("imageLinks", images != null && images.size() > 0?images.get(0):"");				
				String status = item.getStatus();
				resultVal.put("status", status);
				resultVal.put("state", status.equals("Active")? "Success": "Error");
				itemDetailsList.add(resultVal);
			}
			return itemDetailsList;	
		}
	}

	@Override
	public Object updateItemDetailsAdmin(ItemDetails itemDetails) {
		Map<String, Object> returnVal = new HashMap<String, Object>();
		if(itemDetails.getId() == null) {
			throw new RequiredValueMissingException();
		}
		ItemDetails dbItemDetails = itemDetailsRepo.findFirstId(itemDetails.getId());
		
		if(dbItemDetails == null) {
			throw new ItemNotFoundException();
		}
		
		if(itemDetails.getCategoryId() != null) {
			if (!itemCategoryService.checkItemCategory(itemDetails.getCategoryId())) {
				throw new CategoryNotFoundException();
			} else {
				dbItemDetails.setCategoryId(itemDetails.getCategoryId());
			}
		}
		if(itemDetails.getSubCategoryId() != null) {
			if (!itemSubCategoryService.checkItemSubCategory(itemDetails.getSubCategoryId())) {
				throw new SubCategoryNotFoundException();
			} else
				dbItemDetails.setSubCategoryId(itemDetails.getSubCategoryId());
		}

		if (itemDetails.getName() != null && !itemDetails.getName().equals("")) {
			dbItemDetails.setName(itemDetails.getName());
		}
		if (itemDetails.getDescription() != null) {
			ItemDescription itemDescription = itemDetails.getDescription();
			if(itemDetails.getDescription().getDisclaimer() != null && 
					!itemDetails.getDescription().getDisclaimer().equals("")) {
				itemDescription.setDisclaimer(itemDetails.getDescription().getDisclaimer());
			}
			if(itemDetails.getDescription().getItemProperties() != null && 
					!itemDetails.getDescription().getItemProperties().equals("")) {
				itemDescription.setItemProperties(itemDetails.getDescription().getItemProperties());
			}
			if(itemDetails.getDescription().getSellerName() != null && 
					!itemDetails.getDescription().getSellerName().equals("")) {
				itemDescription.setSellerName(itemDetails.getDescription().getSellerName());
			}
			dbItemDetails.setDescription(itemDescription);
		}
		if(itemDetails.getStatus() != null && !itemDetails.getStatus().equals("")) {
			dbItemDetails.setStatus(itemDetails.getStatus()); 
		}
		itemDetailsRepo.save(dbItemDetails);
		
		returnVal.put("id", dbItemDetails.getId());
		returnVal.put("name", dbItemDetails.getName());
		returnVal.put("categoryId", dbItemDetails.getCategoryId());
		returnVal.put("subCategoryId", dbItemDetails.getSubCategoryId());
		returnVal.put("description", dbItemDetails.getDescription());
		returnVal.put("itemAvailabilities", itemAvailabilityService.
				getAvailabilityByItemId(dbItemDetails.getId()));
		returnVal.put("categoryName", itemCategoryService.
				getItemCategoryName(dbItemDetails.getCategoryId()));
		returnVal.put("subCategoryName", itemSubCategoryService.
				getItemSubCategoryName(dbItemDetails.getSubCategoryId()));
		returnVal.put("status", dbItemDetails.getStatus());
		returnVal.put("state", dbItemDetails.getStatus().equals("Active")?
				"Success":
					"Error");
		return returnVal;
	}

	@Override
	public Object addItemDetailsAdmin(ItemDetails itemDetails) {
		Map<String, Object> returnVal = new HashMap<String, Object>();

		if(itemDetails.getCategoryId() != null) {
			if (!itemCategoryService.checkItemCategory(itemDetails.getCategoryId())) {
				throw new CategoryNotFoundException();
			}
		}
		if(itemDetails.getSubCategoryId() != null) {
			if (!itemSubCategoryService.checkItemSubCategory(itemDetails.getSubCategoryId())) {
				throw new SubCategoryNotFoundException();
			}
		}
		if(itemDetails.getStatus() == null || itemDetails.getStatus().equals("")) {
			itemDetails.setStatus("Active");			
		}
		
		ItemDetails dbItemDetails = itemDetailsRepo.save(itemDetails);
				
				returnVal.put("id", dbItemDetails.getId());
				returnVal.put("name", dbItemDetails.getName());
				returnVal.put("categoryId", dbItemDetails.getCategoryId());
				returnVal.put("subCategoryId", dbItemDetails.getSubCategoryId());
				returnVal.put("description", dbItemDetails.getDescription());
				returnVal.put("itemAvailabilities", itemAvailabilityService.
						getAvailabilityByItemId(dbItemDetails.getId()));
				returnVal.put("categoryName", itemCategoryService.
						getItemCategoryName(dbItemDetails.getCategoryId()));
				returnVal.put("subCategoryName", itemSubCategoryService.
						getItemSubCategoryName(dbItemDetails.getSubCategoryId()));
				returnVal.put("status", dbItemDetails.getStatus());
				returnVal.put("state", dbItemDetails.getStatus().equals("Active")?
						"Success":
							"Error");
		return returnVal;
	}

}
