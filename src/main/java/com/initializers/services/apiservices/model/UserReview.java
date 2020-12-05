package com.initializers.services.apiservices.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.initializers.services.apiservices.model.UserCart.CompositeKey;

@Document("user_review")
public class UserReview {

	@Transient
    public static final String SEQUENCE_NAME = "users_review_sequence";
	
	@Id
    private CompositeKey id;
	
	private int rating;
	private String review;
    public static class CompositeKey implements Serializable {
    	private Long userId;
    	private Long itemId;
    	public Long getUserId() {
    		return userId;
    	}
    	public void setUserId(Long userId) {
    		this.userId = userId;
    	}
    	public Long getItemId() {
    		return itemId;
    	}
    	public void setItemId(Long itemId) {
    		this.itemId = itemId;
    	}
    }	
	public CompositeKey getId() {
		return id;
	}
	public void setId(CompositeKey id) {
		this.id = id;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	
	
}
