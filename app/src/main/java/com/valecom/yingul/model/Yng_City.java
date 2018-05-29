package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_City implements Serializable {

	private int cityId;
	private String name;
	private String codigopostal;
    private Yng_Province yng_Province;
	
	public Yng_City() {
		super();
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCodigopostal() {
		return codigopostal;
	}

	public void setCodigopostal(String codigopostal) {
		this.codigopostal = codigopostal;
	}

	public Yng_Province getYng_Province() {
		return yng_Province;
	}

	public void setYng_Province(Yng_Province yng_Province) {
		this.yng_Province = yng_Province;
	}
}

