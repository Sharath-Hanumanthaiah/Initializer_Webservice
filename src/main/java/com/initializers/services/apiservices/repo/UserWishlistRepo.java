package com.initializers.services.apiservices.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.initializers.services.apiservices.model.UserWishlist;

public interface UserWishlistRepo extends MongoRepository<UserWishlist, Long>{

	UserWishlist findFirstByUserID(Long userID);
	
}
