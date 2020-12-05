package com.initializers.services.apiservices.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.initializers.services.apiservices.model.item.ItemSubCategory;

@Component
public class ItemSubCategoryConvertor implements Converter<String, ItemSubCategory> {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public ItemSubCategory convert(String source) {
		ItemSubCategory itemSubCategory = new ItemSubCategory();
		try {
			itemSubCategory = objectMapper.readValue(source, ItemSubCategory.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itemSubCategory;
	}
}
