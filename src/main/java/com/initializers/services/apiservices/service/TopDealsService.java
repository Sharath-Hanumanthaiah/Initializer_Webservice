package com.initializers.services.apiservices.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.model.TopDeals;

@Service
public interface TopDealsService {
	
	List<TopDeals> getTopDeals();
	
	TopDeals addNewTopDeals(TopDeals topDeals);
}
