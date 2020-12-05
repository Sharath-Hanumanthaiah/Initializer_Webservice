package com.initializers.services.apiservices.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.initializers.services.apiservices.model.item.ItemSubCategory;

public interface ItemSubCategoryRepo extends MongoRepository<ItemSubCategory, Long>{

	void deleteByCategoryId(Long categoryId);
	
	ItemSubCategory findFirstById(Long id);
	
	List<ItemSubCategory> findByCategoryId(Long categoryId, Pageable pageable);
	
	ItemSubCategory findTopByCategoryIdOrderByOfferDesc(Long categoryId);
	
	@Query(value = "{'categoryId' : ?0}", fields = "{'id' : ?0}")
	List<ItemSubCategory> checkByCategoryId(Long categoryId);
	
	@Query(value = "{'id' : ?0}", fields = "{ 'name' : ?0 }")
	ItemSubCategory findFirstNameById(Long id);
}
