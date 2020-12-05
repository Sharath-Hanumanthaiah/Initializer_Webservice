package com.initializers.services.apiservices.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.initializers.services.apiservices.model.item.ItemAvailability;

public interface ItemAvailabilityRepo extends MongoRepository<ItemAvailability, Long>{
	
	@Query(value = "{'id' : ?0, 'available' : 'Y'}")
	ItemAvailability findFisrtById(Long id);
	
	@Query(value = "{'id' : ?0 }")
	ItemAvailability findFirstId(Long id);
	
	@Query(value = "{'id' : ?0, 'available' : 'Y'}", fields = "{'discountPrice' : ?0}")
	ItemAvailability findFirstDiscountPriceById(Long id);
	
	@Query(value = "{'id' : ?0}", fields = "{'value' : ?0, 'unit' : ?0}")
	ItemAvailability findFirstValueUnitId(Long id);
	
	@Query(value = "{'itemId' : ?0, 'available' : 'Y'}")
	List<ItemAvailability> findByItem(Long itemId);
	
	List<ItemAvailability> findByItemId(Long itemId);
	
	ItemAvailability findTopByItemIdOrderByDiscountDesc(Long itemId);
}
