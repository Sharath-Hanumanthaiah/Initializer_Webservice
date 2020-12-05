package com.initializers.services.apiservices.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.initializers.services.apiservices.model.item.ItemAvailability;

@Component
public class ItemAvailabilityConvertor implements Converter<String, ItemAvailability>{

    @Autowired
    private ObjectMapper objectMapper;
    
	@Override
	public ItemAvailability convert(String source) {
		ItemAvailability itemAvailability = new ItemAvailability();
		try {
			itemAvailability = objectMapper.readValue(source, ItemAvailability.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itemAvailability;
	}
}
