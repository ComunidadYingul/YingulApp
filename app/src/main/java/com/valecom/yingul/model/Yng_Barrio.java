package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_Barrio implements Serializable {

	private int barrioId;
	private String name;
    private Yng_City yng_City;

	public Yng_Barrio() {
		super();
	}

	public int getBarrioId() {
		return barrioId;
	}

	public void setBarrioId(int barrioId) {
		this.barrioId = barrioId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Yng_City getYng_City() {
		return yng_City;
	}

	public void setYng_City(Yng_City yng_City) {
		this.yng_City = yng_City;
	}
}
