package com.initializers.services.apiservices.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user-order")
public class UserOrderSet { 


	@Transient
    public static final String SEQUENCE_NAME = "users_order";
	@Id
	private Long id;
	private List<UserOrder> orderList;
	private Long addressId;
	private OrderStatus status;
	private Date orderAt;
	private Date deliveredBy;
	private Float totalAmount;
	private Float deliveryCharge;
	private String coupenCode;
	private Float coupenDiscount;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<UserOrder> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<UserOrder> orderList) {
		this.orderList = orderList;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
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
	public Float getDeliveryCharge() {
		return deliveryCharge;
	}
	public void setDeliveryCharge(Float deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}
	public String getCoupenCode() {
		return coupenCode;
	}
	public void setCoupenCode(String coupenCode) {
		this.coupenCode = coupenCode;
	}
	public Float getCoupenDiscount() {
		return coupenDiscount;
	}
	public void setCoupenDiscount(Float coupenDiscount) {
		this.coupenDiscount = coupenDiscount;
	}
	
}
