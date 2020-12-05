package com.initializers.services.apiservices.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.model.UserCart;

@Service
public interface UserCartService {

	UserCart addUserCart(UserCart userCart);
	
	List<Object> getUserCartByuserId(Long userId, Pageable pageable);
	
	Object createOrder(Long userId, Long addressId,String coupenCode);
}
