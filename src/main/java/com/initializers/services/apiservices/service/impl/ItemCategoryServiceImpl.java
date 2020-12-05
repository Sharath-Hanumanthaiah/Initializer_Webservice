package com.initializers.services.apiservices.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.initializers.services.apiservices.exception.CategoryNotFoundException;
import com.initializers.services.apiservices.model.CloudinaryConfig;
import com.initializers.services.apiservices.model.ItemCategorySubCategory;
import com.initializers.services.apiservices.model.item.ItemCategory;
import com.initializers.services.apiservices.model.item.ItemDetailsTemp;
import com.initializers.services.apiservices.others.FilterAction;
import com.initializers.services.apiservices.others.FilterValue;
import com.initializers.services.apiservices.others.Suggestions;
import com.initializers.services.apiservices.repo.ItemCategoryRepo;
import com.initializers.services.apiservices.service.ItemCategoryService;
import com.initializers.services.apiservices.service.ItemDetailsService;
import com.initializers.services.apiservices.service.ItemSubCategoryService;
import com.mongodb.BasicDBObject;

@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {

	@Autowired
	private CloudinaryConfig cloudinaryConfig;
	@Autowired 
	private ItemCategoryRepo itemCategoryRepo;
	@Autowired
	private ItemSubCategoryService itemSubCategoryService;
	@Autowired
	private ItemDetailsService itemDetailsService;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public Object addItemCategory(ItemCategory itemCategory, MultipartFile image) {
		Map<String, Object> returnVal = new HashMap<String, Object>();
		returnVal.put("Info", "item category added successfully");
		
			try {
				String imageUrl = cloudinaryConfig.addImage(image);
				if(imageUrl != null) {
					itemCategory.setImageLink(imageUrl);
				}
			} catch (IOException e1) {
				returnVal.put("Error", e1);
				return returnVal;
			}
		return itemCategoryRepo.save(itemCategory);
	}

	@Override
	public Object getItemCategory(Pageable pageable) {
		List<Object> itemCategory = new ArrayList<>();
		
		List<ItemCategorySubCategory> itemCategorySubCategory = itemCategoryRepo.findItemCategoryJoinSubCategory(pageable);
		
//		return itemCategorySubCategory; 
		for(ItemCategorySubCategory item : itemCategorySubCategory) {
			Map<String, Object> returnVal = new HashMap<>();
			returnVal.put("id", item.getId());
			returnVal.put("name", item.getName());
			returnVal.put("description", item.getDescription());
			returnVal.put("imageLink", item.getImageLink());
			returnVal.put("contain", item.getSubCategoryID().size() > 0 ? "subCategory" : "item");
			itemCategory.add(returnVal);
		};
//		return new PageImpl<>(itemCategory, pageable, itemCategory.size());
		return itemCategory;
	}
	
	@Override
	public ItemCategory getItemCategoryById(Long id) {
		ItemCategory itemCategory = itemCategoryRepo.findFirstById(id);
		if(itemCategory == null) {
			throw new CategoryNotFoundException();
		}else {
			return itemCategory;
		}
	}

	@Override
	public Map<String, Object> deleteItemCategoryById(Long categoryId) {
		Map<String, Object> returnVal = new HashMap<>();
		returnVal.put("Info", "Category deleted by id");
		itemCategoryRepo.deleteById(categoryId);
		itemSubCategoryService.deleteItemSubCategoryByCategoryId(categoryId);
		return returnVal;
	}

	@Override
	public Map<String, Object> updateItemCategoryImage(MultipartFile image, Long categoryId) {
		Map<String, Object> returnVal = new HashMap<>();
		
		ItemCategory itemCategory = itemCategoryRepo.findFirstById(categoryId);
		if(itemCategory == null) {
			returnVal.put("Error", "category doesn't exist");
		}else {
			
			try {
				String imageUrl = cloudinaryConfig.addImage(image);
				if(imageUrl != null) {
					itemCategory.setImageLink(imageUrl);
					itemCategoryRepo.save(itemCategory);
					returnVal.put("Info", "image updated successfully");
				}
			} catch (IOException e1) {
				returnVal.put("Error", e1);
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return returnVal;
	}

	@Override
	public Map<String, Object> updateItemCategoryDescription(ItemCategory itemCategory, Long categoryId) {
		
		Map<String, Object> returnVal = new HashMap<>();
		
		ItemCategory dbItemCategory = itemCategoryRepo.findFirstById(categoryId);
		if(dbItemCategory == null) {
			returnVal.put("Error", "category doesn't exist");
		}else {
			dbItemCategory.setDescription(itemCategory.getDescription());
			itemCategoryRepo.save(dbItemCategory);
			returnVal.put("Info", "description updated successfully");
		}
		return returnVal;
	}

	@Override
	public Map<String, Object> updateCategory(ItemCategory itemCategory, MultipartFile image) {
		Map<String, Object> returnVal = new HashMap<>();
		
		ItemCategory dbItemCategory = itemCategoryRepo.findFirstById(itemCategory.getId());
		if(dbItemCategory == null) {
			returnVal.put("Error", "category doesn't exist");
		}else {
			if(!image.isEmpty()) {
				try {
					String imageUrl = cloudinaryConfig.addImage(image);
					if(imageUrl != null) {
						dbItemCategory.setImageLink(imageUrl);
						itemCategoryRepo.save(itemCategory);
						returnVal.put("Info", "image updated successfully");
					}
				} catch (IOException e1) {
					returnVal.put("Error", e1);
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			final String name = itemCategory.getName();
			final String description = itemCategory.getDescription();
			
			if(name != dbItemCategory.getName() && (name != null && name != "")) {
				dbItemCategory.setName(name);
			}
			if(description != dbItemCategory.getDescription() && description != null) {
				dbItemCategory.setDescription(description);
			}
			
			itemCategoryRepo.save(dbItemCategory);
			returnVal.put("Info", "updated successfully");
		}
		return returnVal;
	}

	@Override
	public boolean checkItemCategory(Long itemCategoryId) {
		if(itemCategoryRepo.findFirstById(itemCategoryId) != null) {
			return true;
		}
		return false;
	}

	@Override
	public void updateItemCategoryOffer(Long itemId,Long offer) {
		ItemDetailsTemp itemDetails = itemDetailsService.getItemDetails(itemId);
		ItemCategory itemCategory = getItemCategoryById(itemDetails.getCategoryId());
		 
		if(itemCategory != null) {
			if(itemCategory.getOffer() == null) {
				itemCategory.setOffer(offer);
			}else {
				if(itemCategory.getOffer() < offer) {
					itemCategory.setOffer(offer);
				}
			}
			itemCategoryRepo.save(itemCategory);
		}

//		ItemCategory itemCategory = getItemCategoryById(categoryId);
//		ItemSubCategory itemSubCategory = itemSubCategoryService.
//				getMaxOfferItemSubCategoryByCategoryId(categoryId);
//		if(itemSubCategory != null) {
//			itemCategory.setOffer(itemSubCategory.getOffer());
//			itemCategoryRepo.save(itemCategory);
//		}
	}

	@Override
	public void getItemCategorySubCategory() {
		// TODO Auto-generated method stub
	    LookupOperation lookupOperation = LookupOperation.newLookup()
	                        .from("item_sub_category")
	                        .localField("_id")
	                        .foreignField("categoryId")
	                        .as("departments");

	    Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("departments").size(1)) , lookupOperation);
	        List<BasicDBObject> results = mongoTemplate.aggregate(aggregation, "item_category", BasicDBObject.class).getMappedResults();
//	        LOGGER.info("Obj Size " +results.size());
	}

	@Override
	public Object getItemCategory(String[] filter, Suggestions suggestions) {
		List<Object> itemCategoryList = new ArrayList<>();
		List<AggregationOperation> list = new ArrayList<AggregationOperation>();
		if(suggestions.getKey() != null && suggestions.getValue() != null) {
			List<Object> suggestionsList = new ArrayList<>();
			list.add(Aggregation.project(Fields.fields().
					and(Fields.field("key", suggestions.getKey())).
					and(Fields.field("value", suggestions.getValue()))));
			Aggregation aggregation = Aggregation.newAggregation(list);
			suggestionsList = mongoTemplate.aggregate(aggregation, "item_category",
					Suggestions.class)
					.getMappedResults().stream().collect(Collectors.toList());
			return suggestionsList;
		}else {
			if(filter != null) {
				List<FilterValue> filterValues =  new FilterAction().getFilterValue(filter);
				for(FilterValue filterValue : filterValues) {
					List<Object> alteredFilterValue = new ArrayList<>();
					if(filterValue.getName() != null && filterValue.getOperator() != null 
							&& filterValue.getValue() != null) {
						switch (filterValue.getName()) {
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
			list.add(Aggregation.project(Fields.fields("_id","name","description","imageLink")));
			Aggregation aggregation = Aggregation.newAggregation(list);
			List<ItemCategory> itemCategory = mongoTemplate.aggregate(aggregation, "item_category", ItemCategory.class)
					.getMappedResults();
			for(ItemCategory item : itemCategory) {
				Map<String, Object> returnVal = new HashMap<>();
				returnVal.put("id", item.getId());
				returnVal.put("name", item.getName());
				returnVal.put("description", item.getDescription());
				returnVal.put("imageLink", item.getImageLink());
				itemCategoryList.add(returnVal);
			};
//			return new PageImpl<>(itemCategory, pageable, itemCategory.size());
			return itemCategoryList;
		}
	}

	@Override
	public String getItemCategoryName(Long id) {
		ItemCategory itemCategory = itemCategoryRepo.findFirstNameById(id);
		
		return itemCategory == null?
				"":
					itemCategory.getName();
	}

	@Override
	public ItemCategory updateCategoryAdmin(ItemCategory itemCategory) {
		ItemCategory dbItemCategory = itemCategoryRepo.findFirstById(itemCategory.getId());
		if(dbItemCategory == null) {
			throw new CategoryNotFoundException();
		}else {
			final String name = itemCategory.getName();
			final String description = itemCategory.getDescription();
			
			if(name != null && !name.equals("")) {
				dbItemCategory.setName(name);
			}
			if(description != null && !description.equals("")) {
				dbItemCategory.setDescription(description);
			}
			return itemCategoryRepo.save(dbItemCategory);
		}
	}

	@Override
	public ItemCategory addItemCategoryAdmin(ItemCategory itemCategory) {
		return itemCategoryRepo.save(itemCategory);
	}
}
