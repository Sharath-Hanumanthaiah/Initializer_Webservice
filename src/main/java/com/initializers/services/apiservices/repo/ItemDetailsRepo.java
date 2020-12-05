package com.initializers.services.apiservices.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.initializers.services.apiservices.model.item.ItemDetails;

public interface ItemDetailsRepo extends MongoRepository<ItemDetails 	, Long>{
	
	@Query(value = "{'id' : ?0, 'status' : 'Active'}")
	ItemDetails findFirstById(Long id);
		
	@Query(value = "{'id' : ?0}")
	ItemDetails findFirstId(Long id);
	
	@Query(value = "{'id' : ?0}", fields = "{'id' : ?0, 'name' : ?0,"
			+ "'imageLinks' : ?0 }")
	ItemDetails findByIdForAllStatus(Long id);
	
	@Query(value = "{'id' : ?0}", fields = "{'name' : ?0 }")
	ItemDetails findFirstNameById(Long id);
	
	@Query(value = "{'categoryId' : ?0}", fields = "{'id' : ?0, 'name' : ?0,"
			+ "'availability' : ?0,'imageLinks' : ?0, 'status' : ?0 }")
	List<ItemDetails> findByCategoryId(Long categoryID, Pageable pageable);
	
	List<ItemDetails> findBySubCategoryId(Long subCategoryID, Pageable pageable);
	
	@Query(value = "{'name' : { $regex: ?0, $options:'i' }, 'status' : 'Active'}", fields = "{'id' : ?0, 'name' : ?0, 'status' : ?0}")
	List<ItemDetails> findCategoryIdByNameRegex(String name);
	
	@Query(value = "{'id' : ?0, 'status' : 'Active'}", fields = "{'id' : ?0, 'name' : ?0,"
			+ "'imageLinks' : ?0 }")
	ItemDetails findListByIdByStatus(Long id, String status);
	
	@Query(value = "{'id' : ?0}", fields = "{'id' : ?0, 'imageLinks' : ?0 }")
	ItemDetails findFirstByIdAndImageLinks();
	
	
}
