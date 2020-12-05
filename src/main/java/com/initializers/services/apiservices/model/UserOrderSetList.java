package com.initializers.services.apiservices.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class UserOrderSetList {
	@Id
	private Long id;
	private String name;
	private String firstLine;
	private String secondLine;
	private String pinCode;
	private String phoneNumber;
	private OrderStatus status;
	private String statusString;
	private String state;
	private Date orderAt;
	private Date deliveredBy;
	private Float totalAmount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFirstLine() {
		return firstLine;
	}
	public void setFirstLine(String firstLine) {
		this.firstLine = firstLine;
	}
	public String getSecondLine() {
		return secondLine;
	}
	public void setSecondLine(String secondLine) {
		this.secondLine = secondLine;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public String getStatusString() {
		return statusString;
	}
	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getOrderAt() {
		return orderAt;
	}
	public void setOrderAt(Date orderAt) {
		this.orderAt = orderAt;
	}
	public Date getDeliveredBy() {
		return deliveredBy;
	}
	public void setDeliveredBy(Date deliveredBy) {
		this.deliveredBy = deliveredBy;
	}
	public Float getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}
}
