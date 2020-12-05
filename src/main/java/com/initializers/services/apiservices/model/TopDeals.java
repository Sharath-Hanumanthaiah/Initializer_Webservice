package com.initializers.services.apiservices.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author karthik
 *
 */
@Document("top_deals")
public class TopDeals {
	@Transient
    public static final String SEQUENCE_NAME = "top_deals";
	
	
	@Id
	private Long id;
	private String type;
	private Long typeID;
	private String resultType;
	private String image;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getTypeID() {
		return typeID;
	}
	public void setTypeID(Long typeID) {
		this.typeID = typeID;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	
}
