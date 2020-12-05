package com.initializers.services.apiservices.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.initializers.services.apiservices.model.UserWishlist;
import com.initializers.services.apiservices.model.UserWishlistTemp;
import com.initializers.services.apiservices.service.UserWishlistService;

@RestController()
@RequestMapping("/user/wishlist")
@CrossOrigin
public class UserWishlistController {

	@Autowired
	private UserWishlistService userWishlistService;
	
	@GetMapping("/{userId}") // {host:port}//user/wishlist/1
	public Map<String, Object> getUserWishlist(@PathVariable Long userId) {
		return userWishlistService.getUserWishlist(userId);
	}
	@PostMapping("/") 
	public UserWishlist addUserWishlist(@RequestBody UserWishlistTemp userWishlist) {
		return userWishlistService.addUserWishlist(userWishlist);
	}
	@DeleteMapping("/{userID}/{itemID}")
	public Object deleteUserWishlist(@PathVariable Long userID, @PathVariable Long itemID) {
		return  userWishlistService.deleteUserWishlist(userID, itemID);
	}
}	
