package com.initializers.services.apiservices.others;

import java.util.List;

public class FilterValue {
	
	private String name;
	private String operator;
	private List<String> value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public List<String> getValue() {
		return value;
	}
	public void setValue(List<String> value) {
		this.value = value;
	}
	
	
}
