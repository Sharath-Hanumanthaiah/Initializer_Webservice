package com.initializers.services.apiservices.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.initializers.services.apiservices.model.UserReview;
import com.initializers.services.apiservices.model.UserReviewTemp;

@Service
public interface UserReviewService {

	List<Object> findReviewByItemId(Long itemId, Pageable pageable);
	
	UserReview addUserReview(UserReviewTemp userReviewTemp);
	
	
}
