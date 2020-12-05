package com.initializers.services.apiservices.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.model.TopDeals;
import com.initializers.services.apiservices.service.TopDealsService;
import com.initializers.services.apiservices.repo.TopDealsRepo;

@Service
public class TopDealsServiceImpl implements TopDealsService {

	@Autowired
	private TopDealsRepo topDealsRepo;
	
	@Override
	public List<TopDeals> getTopDeals() {
		
		// TODO Auto-generated method stub
		return topDealsRepo.findAll();
	}

	@Override
	public TopDeals addNewTopDeals(TopDeals topDeals) {
		// TODO Auto-generated method stub
		return topDealsRepo.save(topDeals);
	}

}
