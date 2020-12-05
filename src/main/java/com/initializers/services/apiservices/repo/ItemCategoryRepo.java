package com.initializers.services.apiservices.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.initializers.services.apiservices.model.item.ItemCategory;

public interface ItemCategoryRepo extends MongoRepository<ItemCategory, Long>, ItemCategoryCustomRepo{
	
	ItemCategory findFirstById(Long id);
	
	@Query(value = "{'id' : ?0}", fields = "{ 'name' : ?0 }")
	ItemCategory findFirstNameById(Long id);
	
	Page<ItemCategory> findAll(Pageable pageable);
}
