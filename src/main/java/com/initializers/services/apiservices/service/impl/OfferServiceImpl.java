package com.initializers.services.apiservices.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.initializers.services.apiservices.model.Offer;
import com.initializers.services.apiservices.repo.OfferRepo;
import com.initializers.services.apiservices.service.OfferService;

public class OfferServiceImpl implements OfferService {

	@Autowired
	private OfferRepo offerRepo;
	
	@Override
	public Offer getOfferByItemId(Long itemId) {
		return null;
	}

	@Override
	public Map<String, Object> addOffer(Offer offer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateOffer(Offer offer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteOffer(Long offerId) {
		// TODO Auto-generated method stub
		return null;
	}

}
