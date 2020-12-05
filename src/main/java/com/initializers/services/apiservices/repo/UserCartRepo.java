package com.initializers.services.apiservices.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.initializers.services.apiservices.model.UserCart;

public interface UserCartRepo extends MongoRepository<UserCart, UserCart.CompositeKey>{

//	@Query(value = "{'_id' : {'userId' : ?0, 'itemId' : '2', 'availabilityId' : '1'}}")
//	List<UserCart> findByUserId(Long userId);
	
	List<UserCart> findByIdUserId(Long userId, Pageable pageable);
	
	List<UserCart> findByIdUserId(Long userId);
	void deleteById(UserCart.CompositeKey id);
}
