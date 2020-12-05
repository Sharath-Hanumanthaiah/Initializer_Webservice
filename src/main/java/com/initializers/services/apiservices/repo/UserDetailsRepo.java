package com.initializers.services.apiservices.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.initializers.services.apiservices.model.UserDetails;

public interface UserDetailsRepo extends MongoRepository<UserDetails, Long>{

	@Query(value = "{'id' : ?0, 'status' : 'Active'}")
	UserDetails findFirstById(Long userId);
	
	@Query(value = "{'id' : ?0, 'status' : 'Active'}", fields = "{'firstName' : ?0, 'lastName' : ?0}")
	UserDetails findNameById(Long userId);
	
	UserDetails findFirstIdByEmail(String email);
	 
	List<UserDetails> findByEmail(String email);
}
