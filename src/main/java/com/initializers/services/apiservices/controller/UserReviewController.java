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

import com.initializers.services.apiservices.model.UserReview;
import com.initializers.services.apiservices.model.UserReviewTemp;
import com.initializers.services.apiservices.service.UserReviewService;

@RestController()
@RequestMapping("/user/review")
@CrossOrigin
public class UserReviewController {
	
	@Autowired
	private UserReviewService userReviewService;
	
	@PostMapping("/")
	public UserReview addUserReview(@RequestBody UserReviewTemp userReview) {
		return userReviewService.addUserReview(userReview);
	}
	@GetMapping("/{itemId}")
	public List<Object> getUserReviewByItem(@PathVariable Long itemId, Pageable pageable) {
		return userReviewService.findReviewByItemId(itemId, pageable);
	}
}
