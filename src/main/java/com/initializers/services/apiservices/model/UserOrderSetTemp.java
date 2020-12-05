package com.initializers.services.apiservices.model;

import java.util.Date;

public class UserOrderSetTemp {
	
	private Long id;
	private String orderList;
	private OrderStatus status;
	private Date deliveredBy;
	private Float totalAmount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrderList() {
		return orderList;
	}
	public void setOrderList(String orderList) {
		this.orderList = orderList;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
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
