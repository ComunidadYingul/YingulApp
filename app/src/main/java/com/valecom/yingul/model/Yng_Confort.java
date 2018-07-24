package com.valecom.yingul.model;

import java.util.HashSet;
import java.util.Set;

public class Yng_Confort {
    private int confortId;
	private String name;
    private Set<Yng_MotorizedConfort> motorizedConfort = new HashSet<>();

    public Yng_Confort(){
    	super();
	}

	public int getConfortId() {
		return confortId;
	}

	public void setConfortId(int confortId) {
		this.confortId = confortId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Yng_MotorizedConfort> getMotorizedConfort() {
		return motorizedConfort;
	}

	public void setMotorizedConfort(Set<Yng_MotorizedConfort> motorizedConfort) {
		this.motorizedConfort = motorizedConfort;
	}
}
