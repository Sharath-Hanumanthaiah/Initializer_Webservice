package com.initializers.services.apiservices.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.initializers.services.apiservices.model.item.ItemDetails;

@Component
public class ItemDetailsConvertor implements Converter<String, ItemDetails> {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public ItemDetails convert(String source) {
		ItemDetails itemDetails = new ItemDetails();
		try {
			itemDetails = objectMapper.readValue(source, ItemDetails.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itemDetails;
	}

}
