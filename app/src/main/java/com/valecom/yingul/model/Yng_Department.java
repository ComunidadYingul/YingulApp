package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_Department implements Serializable {

	private int departmentId;
	private String name;

	public Yng_Department() {
		super();
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
