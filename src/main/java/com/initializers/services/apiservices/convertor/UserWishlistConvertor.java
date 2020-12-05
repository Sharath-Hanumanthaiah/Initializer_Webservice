package com.initializers.services.apiservices.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.initializers.services.apiservices.model.UserWishlist;

@Component
public class UserWishlistConvertor implements Converter<String, UserWishlist>{

	@Autowired
    private ObjectMapper objectMapper;

	@Override
	public UserWishlist convert(String source) {
		UserWishlist userWishlist = new UserWishlist();
		try {
			userWishlist = objectMapper.readValue(source, UserWishlist.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userWishlist;
	}
	

}
