
package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_Country implements Serializable {

	private int countryId;
	private String name;
	private String zip;
	private boolean toBuy;
	private boolean toSell;
	private String countryCod="";
	private String currency="";
	public Yng_Country() {
		super();
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public boolean isToBuy() {
		return toBuy;
	}

	public void setToBuy(boolean toBuy) {
		this.toBuy = toBuy;
	}

	public boolean isToSell() {
		return toSell;
	}

	public void setToSell(boolean toSell) {
		this.toSell = toSell;
	}

	public String getCountryCod() {
		return countryCod;
	}

	public void setCountryCod(String countryCod) {
		this.countryCod = countryCod;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}

