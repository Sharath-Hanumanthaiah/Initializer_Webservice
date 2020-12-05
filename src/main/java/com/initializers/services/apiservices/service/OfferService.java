package com.initializers.services.apiservices.service;

import java.util.Map;

import com.initializers.services.apiservices.model.Offer;

public interface OfferService {

	//get will be latest offer
	Offer getOfferByItemId(Long itemId);
	
	//add offer calc persent or price based on data
	Map<String, Object> addOffer(Offer offer);
	
	//update offer
	Map<String, Object> updateOffer(Offer offer);
	
	//delete offer
	Map<String, Object> deleteOffer(Long offerId);
}
