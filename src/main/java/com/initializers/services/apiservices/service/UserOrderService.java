package com.initializers.services.apiservices.service;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.model.UserOrderSet;

@Service
public interface UserOrderService {
	
	Object getUserOrderList(Long userId, Pageable pageable);
	
	Object getUserOrder(Long id);
	
	Object getAllUserOrderAdmin(String[] filter);
	
	Object getUserOrderAdmin(Long id);
	
	Map<String, Object> updateUserOrderSet(UserOrderSet userOrderSet);
}
