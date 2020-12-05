package com.initializers.services.apiservices.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.initializers.services.apiservices.model.Address;


public interface AddressRepo extends MongoRepository<Address, Long>{
	
	List<Address> findByUserId(Long userId);
	
	Address findFirstById(Long id);
	
	@Query(value = "{'id' : ?0, 'userId' : ?0}", fields = "{'name' : ?0}")
	Address findByIdUserID(Long id, Long userId);
	
}
