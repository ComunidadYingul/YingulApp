package com.valecom.yingul.model;

import java.io.Serializable;

public class FilterParam implements Serializable {

	private boolean freeShipping;
	private String condition;
	private String discount;
	private Yng_Ubication ubication;
	private Double minPrice;
	private Double maxPrice;

	public FilterParam(){
		super();
	}

	public boolean isFreeShipping() {
		return freeShipping;
	}

	public void setFreeShipping(boolean freeShipping) {
		this.freeShipping = freeShipping;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Yng_Ubication getUbication() {
		return ubication;
	}

	public void setUbication(Yng_Ubication ubication) {
		this.ubication = ubication;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}
}

