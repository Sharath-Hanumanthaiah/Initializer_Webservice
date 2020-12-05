package com.initializers.services.apiservices.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.initializers.services.apiservices.model.UserReview;

public interface UserReviewRepo extends MongoRepository<UserReview, Long>{
	
	List<UserReview> findByIdItemId(Long itemId, Pageable pageable);
}
