package com.initializers.services.apiservices.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.initializers.services.apiservices.model.OrderStatus;
import com.initializers.services.apiservices.model.UserOrderSet;



@Component
public class UserOrderSetConvertor implements Converter<String, UserOrderSet>{

	@Autowired
    private ObjectMapper objectMapper;
	
	@Override
	public UserOrderSet convert(String source) {
		UserOrderSet userOrderSet = new UserOrderSet();
		try {
			JsonNode rootNode;
			userOrderSet = objectMapper.readValue(source, UserOrderSet.class);
//			rootNode = objectMapper.readTree(source);
//			JsonNode status = rootNode.path("status");
//			OrderStatus o = new OrderStatus();
////			o.setConfirmed();
//			System.out.println(status.path("confirmed").asText());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userOrderSet;
	}
}
