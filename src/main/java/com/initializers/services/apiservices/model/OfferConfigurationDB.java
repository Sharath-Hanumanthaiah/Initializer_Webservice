package com.initializers.services.apiservices.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "configurationDB")
public class OfferConfigurationDB {

	@Transient
    public static final String SEQUENCE_NAME = "configuration_sequence";
	
	private Long id;
	private ArrayList<String> fieldIn;
	private String fieldOut;
	private String symbol;
	private String type;
	private String formula;
	private String flag;
	private Boolean round;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ArrayList<String> getFieldIn() {
		return fieldIn;
	}
	public void setFieldIn(ArrayList<String> fieldIn) {
		this.fieldIn = fieldIn;
	}
	public String getFieldOut() {
		return fieldOut;
	}
	public void setFieldOut(String fieldOut) {
		this.fieldOut = fieldOut;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public Boolean getRound() {
		return round;
	}
	public void setRound(Boolean round) {
		this.round = round;
	}
	
}
