package com.initializers.services.apiservices.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.model.UserWishlist;
import com.initializers.services.apiservices.model.UserWishlistTemp;

@Service
public interface UserWishlistService {

	UserWishlist addUserWishlist(UserWishlistTemp userWishlist);
	
	Map<String, Object> getUserWishlist(Long userID);
	
	Object deleteUserWishlist(Long userID, Long itemID);
	
}
