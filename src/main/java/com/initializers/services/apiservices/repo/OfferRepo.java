package com.initializers.services.apiservices.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.initializers.services.apiservices.model.Offer;

public interface OfferRepo extends MongoRepository<Offer, Long>{

}
