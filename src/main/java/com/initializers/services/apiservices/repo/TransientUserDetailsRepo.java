package com.initializers.services.apiservices.repo;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.initializers.services.apiservices.model.TransientUserDetails;

public interface TransientUserDetailsRepo extends MongoRepository<TransientUserDetails, ObjectId>{

	TransientUserDetails findFirstById(long userId);
	
	Long deleteById(long userId);
	
	TransientUserDetails findFirstIdByEmail(String email);
	
}
