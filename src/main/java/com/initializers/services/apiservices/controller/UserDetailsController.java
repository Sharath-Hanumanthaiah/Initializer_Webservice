package com.initializers.services.apiservices.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.initializers.services.apiservices.model.TransientUserDetails;
import com.initializers.services.apiservices.model.UserDetails;
import com.initializers.services.apiservices.service.TransientUserDetailsService;
import com.initializers.services.apiservices.service.UserDetailsService;

@RestController()
@RequestMapping("/user")
@CrossOrigin
public class UserDetailsController {
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private TransientUserDetailsService transientUserService;
	
	
	@GetMapping("/")
	public List<UserDetails> getAllUsers()
	{
		return userDetailsService.getAllUsers();
	}
	@GetMapping("/{userId}")
	public UserDetails getUser(@PathVariable Long userId) {
		return userDetailsService.getUser(userId);
	}
	@PostMapping("/")
	public Map<String,Object> addUser(@RequestBody TransientUserDetails transientUserDetails) {
		return transientUserService.addUser(transientUserDetails);
	}
	@DeleteMapping("/{userId}")
	public Object deleteUser(@PathVariable Long userId) {
		return userDetailsService.deleteUser(userId);
	}
	@PutMapping("/")
	public Map<String,Object> updateUser(@RequestBody UserDetails userDetails) {
		return userDetailsService.updateUserDetail(userDetails);
	}
	
	@PostMapping("/test")
	public void testURL(@RequestBody UserDetails userDetails) {
		userDetailsService.updateUserDetail(userDetails);
	}
}
