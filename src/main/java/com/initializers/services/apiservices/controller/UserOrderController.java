package com.initializers.services.apiservices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.initializers.services.apiservices.model.UserOrder;
import com.initializers.services.apiservices.service.UserOrderService;

@RestController()
@RequestMapping("/user/order")
@CrossOrigin
public class UserOrderController {
	
	@Autowired
	private UserOrderService userOrderService;

	@GetMapping("/{orderId}")
	public Object getOrderbyId(@PathVariable Long orderId) {
		return userOrderService.getUserOrder(orderId);
	}
	
	
	@GetMapping("/list/{userId}")
	public Object getUserOrders(@PathVariable Long userId, Pageable pageable) {
		return userOrderService.getUserOrderList(userId, pageable);
	}
	
	@GetMapping("/test")
	public Object test() {
		UserOrder user = new UserOrder();
//		user.setAmount(120F);
//		user.setItemId(101L);
//		user.setOrderAt(new Date());
//		user.setStatus(new OrderStatus());
//		offerConfigurationDBService.configureOrderBeforeSend(user);
		return "suc";
	}
}
