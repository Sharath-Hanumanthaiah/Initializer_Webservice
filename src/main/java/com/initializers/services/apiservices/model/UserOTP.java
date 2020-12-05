package com.initializers.services.apiservices.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


@Document("UserOTP")
public class UserOTP {
	
	@Id
	private long id;
	
	private Integer otp;
	
	@DateTimeFormat(iso=ISO.DATE_TIME)
	private java.util.Date expiryTime;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Integer getOTP() {
		return otp;
	}
	public void setOTP(Integer OTP) {
		this.otp = OTP;
	}
	public java.util.Date getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(java.util.Date expiryTime) {
		this.expiryTime = expiryTime;
	}	
}
