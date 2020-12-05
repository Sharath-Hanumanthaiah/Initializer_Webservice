package com.initializers.services.apiservices.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.initializers.services.apiservices.model.item.ItemCategory;

@Component
public class ItemCategoryConvertor implements Converter<String, ItemCategory>{

    @Autowired
    private ObjectMapper objectMapper;

	@Override
	public ItemCategory convert(String source) {
		ItemCategory itemCategory = new ItemCategory();
		try {
			itemCategory = objectMapper.readValue(source, ItemCategory.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itemCategory;
	}
    
    
}
