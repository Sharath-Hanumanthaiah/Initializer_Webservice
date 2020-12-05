package com.initializers.services.apiservices.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.exception.ItemNotFoundException;
import com.initializers.services.apiservices.exception.RequiredValueMissingException;
import com.initializers.services.apiservices.exception.UserNotFoundException;
import com.initializers.services.apiservices.model.UserReview;
import com.initializers.services.apiservices.model.UserReviewTemp;
import com.initializers.services.apiservices.repo.UserReviewRepo;
import com.initializers.services.apiservices.service.ItemDetailsService;
import com.initializers.services.apiservices.service.UserDetailsService;
import com.initializers.services.apiservices.service.UserReviewService;

@Service
public class UserReviewServiceImpl implements UserReviewService {

	@Autowired
	private UserReviewRepo userReviewRepo;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private ItemDetailsService itemDetailsService;
	
	@Override
	public List<Object> findReviewByItemId(Long itemId, Pageable pageable) {
		// TODO item exist validation
		List<Object> returnVal = new ArrayList<>();
		for(UserReview userReview : userReviewRepo.findByIdItemId(itemId, pageable)) {
			Map<String, Object> reviews = new HashMap<String, Object>();
			reviews.put("user", userDetailsService.getNameById(userReview.getId().getUserId()));
			reviews.put("rating", userReview.getRating());
			reviews.put("review", userReview.getReview());
			returnVal.add(reviews);
		}
		return returnVal;
	}

	@Override
	public UserReview addUserReview(UserReviewTemp userReviewTemp) {
		if(userReviewTemp.getUserId() == null || userReviewTemp.getItemId() == null) {
			throw new RequiredValueMissingException();
		} else if(userDetailsService.getUser(userReviewTemp.getUserId()) == null) {
			throw new UserNotFoundException();
		} else if(itemDetailsService.getItemDetails(userReviewTemp.getItemId()) == null) {
			throw new ItemNotFoundException();
		}else {
			UserReview userReview = new UserReview();
			userReview.setId(new UserReview.CompositeKey());
			userReview.getId().setItemId(userReviewTemp.getItemId());
			userReview.getId().setUserId(userReviewTemp.getUserId());
			userReview.setRating(userReviewTemp.getRating());
			userReview.setReview(userReviewTemp.getReview());
			return userReviewRepo.save(userReview);
		}
	}
}
