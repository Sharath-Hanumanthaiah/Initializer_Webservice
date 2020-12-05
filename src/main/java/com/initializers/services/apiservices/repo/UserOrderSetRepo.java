package com.initializers.services.apiservices.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.initializers.services.apiservices.model.UserOrderSet;

public interface UserOrderSetRepo extends MongoRepository<UserOrderSet, Long>{
	
	UserOrderSet findFirstById(Long id);
	
	List<UserOrderSet> findByOrderListUserId(Long userId, Pageable pageable);
	
}