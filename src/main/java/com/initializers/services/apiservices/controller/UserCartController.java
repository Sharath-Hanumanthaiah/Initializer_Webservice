package com.initializers.services.apiservices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.initializers.services.apiservices.model.UserCart;
import com.initializers.services.apiservices.service.UserCartService;

@RestController()
@RequestMapping("/user/cart")
@CrossOrigin
public class UserCartController {

	@Autowired 
	private UserCartService userCartService;
	
	@GetMapping("/{userId}")
	public List<Object> getUserCartByUser(@PathVariable Long userId, Pageable pageable) {
		return userCartService.getUserCartByuserId(userId, pageable);
	}
	
	@PostMapping("/")
	public UserCart addUserCart(@RequestBody UserCart userCart) {
		return userCartService.addUserCart(userCart);
	}
	
	@PostMapping("/create-order/{userId}/{addressId}")
	public Object createOrder(@PathVariable Long userId,@PathVariable Long addressId,
		 @RequestBody(required = false) String coupenCode) {
		return userCartService.createOrder(userId, addressId, coupenCode);
	}
}
