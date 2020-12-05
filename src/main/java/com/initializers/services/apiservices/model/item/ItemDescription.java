package com.initializers.services.apiservices.model.item;

import org.springframework.stereotype.Component;

@Component
public class ItemDescription {

	private String itemProperties;
	private String sellerName;
	private String disclaimer;
	public String getItemProperties() {
		return itemProperties;
	}
	public void setItemProperties(String itemProperties) {
		this.itemProperties = itemProperties;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getDisclaimer() {
		return disclaimer;
	}
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}
	
	
}
