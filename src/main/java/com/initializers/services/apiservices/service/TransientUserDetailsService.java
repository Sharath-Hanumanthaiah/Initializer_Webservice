package com.initializers.services.apiservices.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.model.TransientUserDetails;

@Service
public interface TransientUserDetailsService {

	Map<String,Object> addUser(TransientUserDetails transientUserDetails);
	
	TransientUserDetails getUser(long userId);
	
	Long deleteUser(long userId);
}
