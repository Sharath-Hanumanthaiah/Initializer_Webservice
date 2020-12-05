package com.initializers.services.apiservices.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.initializers.services.apiservices.model.OfferConfigurationDB;

public interface OfferConfigurationDBRepo extends MongoRepository<OfferConfigurationDB, Long>{
	@Query(value = "{'type' : ?0, 'flag' : 'true'}")
	List<OfferConfigurationDB> findByType(String type);
}