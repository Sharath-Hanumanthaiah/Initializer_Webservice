package com.initializers.services.apiservices.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.model.UserOTP;

@Service
public interface UserOTPService {
	
	void addUserOTP(UserOTP userOTP);
	
	Map<String,Object> validateUserOTP(UserOTP userOTP);

	int pushUserDetails(long userId);
	
	Long deleteUserOTP(long userId);
	
	Map<String,Object> regenerateUserOTP(long userId);
}
