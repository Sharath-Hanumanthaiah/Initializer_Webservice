package com.initializers.services.apiservices.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.initializers.services.apiservices.model.UserOTP;

public interface UserOTPRepo extends MongoRepository<UserOTP, Long> {
	
	UserOTP findFirstById(long id);
	
	Long deleteById(long id);
}
