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
import com.initializers.services.apiservices.exception.SubCategoryNotFoundException;
import com.initializers.services.apiservices.model.CloudinaryConfig;
import com.initializers.services.apiservices.model.item.ItemDetailsTemp;
import com.initializers.services.apiservices.model.item.ItemSubCategory;
import com.initializers.services.apiservices.model.item.ItemSubCategoryList;
import com.initializers.services.apiservices.others.FilterAction;
import com.initializers.services.apiservices.others.FilterValue;
import com.initializers.services.apiservices.others.Suggestions;
import com.initializers.services.apiservices.repo.ItemCategoryRepo;
import com.initializers.services.apiservices.repo.ItemSubCategoryRepo;
import com.initializers.services.apiservices.service.ItemAvailabilityService;
import com.initializers.services.apiservices.service.ItemCategoryService;
import com.initializers.services.apiservices.service.ItemDetailsService;
import com.initializers.services.apiservices.service.ItemSubCategoryService;

@Service
public class ItemSubCategoryServiceImpl implements ItemSubCategoryService {

	@Autowired
	private CloudinaryConfig cloudinaryConfig;
	@Autowired
	private ItemSubCategoryRepo itemSubCategoryRepo;
	@Autowired
	private ItemCategoryRepo itemCategoryRepo;
	@Autowired
	private ItemDetailsService itemDetailsService;
	@Autowired
	private ItemCategoryService itemCategoryService;
	@Autowired
	private ItemAvailabilityService itemAvailabilityService;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Object addItemSubCategory(ItemSubCategory itemSubCategory, MultipartFile image) {
		Map<String, Object> returnVal = new HashMap<String, Object>();
		if (itemCategoryRepo.findFirstById(itemSubCategory.getCategoryId()) == null) {
			returnVal.put("Error", "category doesn't exist");
			return returnVal;
		} else {
			if (!image.isEmpty()) {
				String imageUrl;
				try {
					imageUrl = cloudinaryConfig.addImage(image);
					if (imageUrl != null) {
						itemSubCategory.setImageLink(imageUrl);
					}
				} catch (IOException e1) {
					returnVal.put("Error", e1);
					return returnVal;
				}
			}
			return itemSubCategoryRepo.save(itemSubCategory);
		}
	}

	@Override
	public List<Object> getItemSubCategory(String[] filter, Suggestions suggestions) {
		List<AggregationOperation> list = new ArrayList<AggregationOperation>();
		if (suggestions.getKey() != null && suggestions.getValue() != null) {
			List<Object> suggestionsList = new ArrayList<>();
			list.add(Aggregation.project(Fields.fields().and(Fields.field("key", suggestions.getKey()))
					.and(Fields.field("value", suggestions.getValue()))));
			Aggregation aggregation = Aggregation.newAggregation(list);
			suggestionsList = mongoTemplate.aggregate(aggregation, "item_sub_category", Suggestions.class)
					.getMappedResults().stream().collect(Collectors.toList());
			return suggestionsList;
		} else {
			list.add(LookupOperation.newLookup().from("item_category").localField("categoryId").foreignField("_id")
					.as("join_cat"));
			if (filter != null) {
				List<FilterValue> filterValues = new FilterAction().getFilterValue(filter);
				for (FilterValue filterValue : filterValues) {
					List<Object> alteredFilterValue = new ArrayList<>();
					if (filterValue.getName() != null && filterValue.getOperator() != null
							&& filterValue.getValue() != null) {
						switch (filterValue.getName()) {
						case "categoryId":
							alteredFilterValue = filterValue.getValue().stream().map(Long::parseLong)
									.collect(Collectors.toList());
							break;
						case "_id":
							alteredFilterValue = filterValue.getValue().stream().map(Long::parseLong)
									.collect(Collectors.toList());
							break;
						default:
							alteredFilterValue = filterValue.getValue().stream().collect(Collectors.toList());
							break;
						}
						list.add(Aggregation.match(Criteria.where(filterValue.getName()).in(alteredFilterValue)));
					}
				}
			}
			list.add(Aggregation.project(Fields.fields("_id", "description", "name", "imageLink", "offer")
					.and(Fields.field("categoryId", "join_cat.name"))));
			Aggregation aggregation = Aggregation.newAggregation(list);
			List<ItemSubCategoryList> itemSubCategory = mongoTemplate
					.aggregate(aggregation, "item_sub_category", ItemSubCategoryList.class).getMappedResults();
			return itemSubCategory.stream().collect(Collectors.toList());
		}
//		return itemSubCategoryRepo.findAll().stream().collect(Collectors.toList());
	}

	@Override
	public ItemSubCategory getItemSubCategoryById(Long id) {
		return itemSubCategoryRepo.findFirstById(id);
	}

	@Override
	public Map<String, Object> deleteItemSubCategoryByCategoryId(Long categoryId) {
		Map<String, Object> returnVal = new HashMap<>();
		returnVal.put("Info", "Sub category got deleted by category id");
		itemSubCategoryRepo.deleteByCategoryId(categoryId);
		return returnVal;
	}

	@Override
	public Map<String, Object> deleteItemSubCategoryById(Long subCategoryId) {
		// TODO Auto-generated method stub
		Map<String, Object> returnVal = new HashMap<>();
		returnVal.put("Info", "Sub category deleted by id");
		itemSubCategoryRepo.deleteById(subCategoryId);
		return returnVal;
	}

	@Override
	public Map<String, Object> updateItemSubCategoryImage(MultipartFile image, Long subCategoryId) {
		Map<String, Object> returnVal = new HashMap<>();

		ItemSubCategory itemSubCategory = itemSubCategoryRepo.findFirstById(subCategoryId);
		if (itemSubCategory == null) {
			returnVal.put("Error", "subcategory doesn't exist");
		} else {

			try {
				String imageUrl = cloudinaryConfig.addImage(image);
				if (imageUrl != null) {
					itemSubCategory.setImageLink(imageUrl);
					itemSubCategoryRepo.save(itemSubCategory);
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
	public List<ItemSubCategory> getItemSubCategoryByCategoryId(Long categoryId, Pageable pageable) {
		return itemSubCategoryRepo.findByCategoryId(categoryId, pageable);
	}

	@Override
	public Map<String, Object> updateItemSubCategoryDescription(ItemSubCategory itemSubCategory, Long subCategoryId) {

		Map<String, Object> returnVal = new HashMap<>();
		ItemSubCategory dbItemSubCategory = itemSubCategoryRepo.findFirstById(subCategoryId);
		if (dbItemSubCategory == null) {
			returnVal.put("Error", "sub category doesn't exist");
		} else {
			dbItemSubCategory.setDescription(itemSubCategory.getDescription());
			itemSubCategoryRepo.save(dbItemSubCategory);
			returnVal.put("Info", "description updated successfully");
		}
		return returnVal;
	}

	@Override
	public Map<String, Object> updateSubCategory(ItemSubCategory itemSubCategory, MultipartFile image) {
		Map<String, Object> returnVal = new HashMap<>();

		ItemSubCategory dbItemSubCategory = itemSubCategoryRepo.findFirstById(itemSubCategory.getId());
		if (dbItemSubCategory == null) {
			returnVal.put("Error", "sub category doesn't exist");
		} else {
			if (!image.isEmpty()) {
				try {
					String imageUrl = cloudinaryConfig.addImage(image);
					if (imageUrl != null) {
						dbItemSubCategory.setImageLink(imageUrl);
						itemSubCategoryRepo.save(itemSubCategory);
						returnVal.put("Info", "image updated successfully");
					}
				} catch (IOException e1) {
					returnVal.put("Error", e1);
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			final String name = itemSubCategory.getName();
			final String description = itemSubCategory.getDescription();
			final Long categoryId = itemSubCategory.getCategoryId();

			if (name != dbItemSubCategory.getName() && name != null) {
				dbItemSubCategory.setName(name);
			}
			if (description != dbItemSubCategory.getDescription() && description != null) {
				dbItemSubCategory.setDescription(description);
			}
			if (categoryId != dbItemSubCategory.getCategoryId() && categoryId != null) {
				if (itemCategoryRepo.findFirstById(categoryId) != null) {
					dbItemSubCategory.setCategoryId(categoryId);
				} else {
					returnVal.put("Error", "CategoryId doesn't exist");
				}
			}
			itemSubCategoryRepo.save(dbItemSubCategory);
			returnVal.put("Info", "updated successfully");
		}
		return returnVal;
	}

	@Override
	public boolean checkItemSubCategory(Long itemSubCategoryId) {
		if (itemSubCategoryRepo.findFirstById(itemSubCategoryId) != null) {
			return true;
		}
		return false;
	}

	@Override
	public void updateItemSubCategoryOffer(Long itemId, Long offer) {

		ItemDetailsTemp itemDetails = itemDetailsService.getItemDetails(itemId);
		ItemSubCategory itemSubCategory = getItemSubCategoryById(itemDetails.getSubCategoryId());

		if (itemSubCategory != null) {
			if (itemSubCategory.getOffer() == null) {
				itemSubCategory.setOffer(offer);
			} else {
				if (itemSubCategory.getOffer() < offer) {
					itemSubCategory.setOffer(offer);
				}
			}
			itemSubCategoryRepo.save(itemSubCategory);
		}
//		ong maxOffer = 0L;
//		List<ItemDetails> itemDetails = itemDetailsService.getItemDetailsBySubCategory(subCategoryID);
//		for(ItemDetails item : itemDetails) {
//			ItemAvailability itemAvailability = itemAvailabilityService.getMaxAvailabilityByItemId(item.
//					getId());
//			if(itemAvailability != null) {
//				if(itemAvailability.getDiscount() > maxOffer) {
//					maxOffer = itemAvailability.getDiscount();
//				}
//			}
//		}
//		itemSubCategory.setOffer(maxOffer);
	}

	@Override
	public ItemSubCategory getMaxOfferItemSubCategoryByCategoryId(Long categoryId) {
		return itemSubCategoryRepo.findTopByCategoryIdOrderByOfferDesc(categoryId);
	}

	@Override
	public String getItemSubCategoryName(Long id) {
		ItemSubCategory itemSubCategory = itemSubCategoryRepo.findFirstNameById(id);

		return itemSubCategory == null ? "" : itemSubCategory.getName();
	}

	@Override
	public ItemSubCategory addItemSubCategoryAdmin(ItemSubCategory itemSubCategory) {
		if (itemCategoryRepo.findFirstById(itemSubCategory.getCategoryId()) == null) {
			throw new SubCategoryNotFoundException();
		} else {
			if (!itemCategoryService.checkItemCategory(itemSubCategory.getCategoryId())) {
				throw new CategoryNotFoundException();
			}
			return itemSubCategoryRepo.save(itemSubCategory);
		}
	}

	@Override
	public ItemSubCategory updateSubCategoryAdmin(ItemSubCategory itemSubCategory) {
		ItemSubCategory dbItemSubCategory = itemSubCategoryRepo.findFirstById(itemSubCategory.getId());
		if (dbItemSubCategory == null) {
			throw new SubCategoryNotFoundException();
		} else {
			final String name = itemSubCategory.getName();
			final String description = itemSubCategory.getDescription();
			final Long categoryId = itemSubCategory.getCategoryId();

			if (name != null && !name.equals("")) {
				dbItemSubCategory.setName(name);
			}
			if (description != null && !description.equals("")) {
				dbItemSubCategory.setDescription(description);
			}
			if (categoryId != null && categoryId != dbItemSubCategory.getCategoryId()) {
				if (!itemCategoryService.checkItemCategory(categoryId)) {
					throw new CategoryNotFoundException();
				}
				dbItemSubCategory.setCategoryId(categoryId);
			}
			return itemSubCategoryRepo.save(dbItemSubCategory);
		}
	}
}
