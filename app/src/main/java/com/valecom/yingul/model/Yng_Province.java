
package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_Province implements Serializable {

    private int provinceId;
	private String name;
	private String codigo31662;
    private Yng_Country yng_Country;

	public Yng_Province() {
		super();
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCodigo31662() {
		return codigo31662;
	}

	public void setCodigo31662(String codigo31662) {
		this.codigo31662 = codigo31662;
	}

	public Yng_Country getYng_Country() {
		return yng_Country;
	}

	public void setYng_Country(Yng_Country yng_Country) {
		this.yng_Country = yng_Country;
	}
}

