package com.initializers.services.apiservices.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.initializers.services.apiservices.model.UserOTP;
import com.initializers.services.apiservices.service.UserOTPService;

@RestController()
@RequestMapping("/user/otp")
@CrossOrigin
public class UserOTPController {

	@Autowired
	private UserOTPService userOTPService;
	
	@PostMapping("/")
	public Map<String,Object> validateUserOTP(@RequestBody UserOTP userOTP) {
		return userOTPService.validateUserOTP(userOTP);
	}
	@GetMapping("/{userId}")
	public Map<String,Object> regenerateOTP(@PathVariable Long userId) {
		return userOTPService.regenerateUserOTP(userId);
	}
}
