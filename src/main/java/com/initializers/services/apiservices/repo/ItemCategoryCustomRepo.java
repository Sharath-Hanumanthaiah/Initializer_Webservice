package com.initializers.services.apiservices.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.initializers.services.apiservices.model.ItemCategorySubCategory;

public interface ItemCategoryCustomRepo {
	
	List<ItemCategorySubCategory> findItemCategoryJoinSubCategory(Pageable pagable);
}
