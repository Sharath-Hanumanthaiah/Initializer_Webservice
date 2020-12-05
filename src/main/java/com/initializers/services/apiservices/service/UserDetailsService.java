package com.initializers.services.apiservices.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.model.UserDetails;

@Service
public interface UserDetailsService {
	

	List<UserDetails> getAllUsers();
	
	Long addUser(UserDetails userDetails);
	
	UserDetails getUser(Long userId);
	
	Object deleteUser(Long userId);
	
	Map<String,Object> updateUserDetail(UserDetails userDetails);
	
	String getNameById(Long userId);
	
}
