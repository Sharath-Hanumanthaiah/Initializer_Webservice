package com.initializers.services.apiservices.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.initializers.services.apiservices.model.TopDeals;

public interface TopDealsRepo extends MongoRepository<TopDeals, Long>{
	
	

}
