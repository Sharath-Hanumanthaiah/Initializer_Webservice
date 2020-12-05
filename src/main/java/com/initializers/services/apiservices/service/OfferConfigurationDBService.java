package com.initializers.services.apiservices.service;

import org.springframework.stereotype.Service;

@Service
public interface OfferConfigurationDBService {

	Object configureOrderBeforeSend(Object object, String type);
}
