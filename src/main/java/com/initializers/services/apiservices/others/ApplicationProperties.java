package com.initializers.services.apiservices.others;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

	Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);
	
	public static String cloudname;
	public static String api_key; 
	public static String api_secret;
	public static String[] orderConfigType = {"DLRCRG","DLRBY","COUPEN","AUTOCNFRM"};
	public static Map<String, String> orderFieldMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -9026411095742084952L;

		{
			put("ItemDetails","UserOrderSet.orderList.itemId");
			put("UserDetails","UserOrderSet.orderList.userId");
			put("Address","UserOrderSet.addressId");
		}
	};
	public ApplicationProperties(@Value("${cloudinary.cloud_name}") String cloudname,
			@Value("${cloudinary.api_key}") String api_key,
			@Value("${cloudinary.api_secret}") String api_secret,
			@Value("${app.env}") String env) {
		logger.info("app running in " + env + " environment");
		ApplicationProperties.cloudname = cloudname;
		ApplicationProperties.api_key = api_key;
		ApplicationProperties.api_secret = api_secret;
	}
}